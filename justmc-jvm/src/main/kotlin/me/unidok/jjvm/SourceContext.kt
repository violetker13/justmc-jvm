package me.unidok.jjvm

import me.unidok.jjvm.model.JJVMConfig
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.jjvm.util.getAnnotation
import me.unidok.justcode.Handlers
import me.unidok.justcode.trigger.EventTrigger
import me.unidok.justcode.trigger.FunctionTrigger
import me.unidok.justcode.trigger.Trigger
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.LocalizedText
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.ValueType
import me.unidok.justcode.value.parameter.Parameter
import me.unidok.justcode.value.parameter.SingleParameter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

class SourceContext(
    val config: JJVMConfig,
    val finalTypes: Set<String>,
    val classByName: Map<String, ClassNode>,
    val classMethodsMaps: Map<ClassNode, Map<String, MethodNode>>,
    val contexts: Map<MethodNode, MethodContext>,
) {
    val handlers = ArrayList<Trigger>()
    val onInit = ArrayList<JustOperation>()

    fun isFinalClass(name: String) = finalTypes.contains(name)

    fun findClass(name: String): ClassNode? {
        return classByName[name]
    }

    fun findClassMethod(clazz: ClassNode, methodName: String, methodDesc: String): MethodNode? {
        return classMethodsMaps[clazz]!![Translator.methodName(clazz.name, methodName, methodDesc)]
    }

    fun writeHandlers(): Handlers {
        for ((className, clazz) in classByName) {
            for (method in clazz.methods) {
                val className = clazz.name
                val methodContext = contexts[method]!!
                val context = TranslationContext(methodContext)
                methodContext.translateOperations(context)
                val justOperations = context.operations

                val methodName = method.name
                if (methodName == "<clinit>") {
                    onInit.addAll(justOperations)
                    continue
                }

                val methodDesc = method.desc
                val methodAccess = method.access
                val isStatic = methodAccess and Opcodes.ACC_STATIC != 0
                val argumentTypes = Type.getArgumentTypes(methodDesc)
                if (isStatic) {
                    val eventHandler = method.getAnnotation(Annotations.EVENT_HANDLER)
                    if (eventHandler != null) {
                        val eventName = eventHandler["id"]
                        if (eventName == null) {
                            if (argumentTypes.size != 1) {
                                throw TranslateException("EventHandler is expecting only one parameter")
                            }
                            val internalName = argumentTypes[0].internalName
                            val event = findClass(internalName)
                                ?.getAnnotation(Annotations.EVENT)?.get("id")?.toString()
                                ?: throw TranslateException("Unknown event '$internalName'")
                            handlers.add(EventTrigger(justOperations, event))
                        } else {
                            handlers.add(EventTrigger(justOperations, eventName.toString()))
                        }
                        continue
                    }
                }

                val parameters = ArrayList<Parameter>()
                var slot = 0
                val returnType = Type.getReturnType(methodDesc)
                if (returnType != Type.VOID_TYPE) {
                    parameters.add(SingleParameter(
                        description = LocalizedText.Data(emptyMap(), TextValue(returnType.className)),
                        name = Translator.RETURN_VARIABLE_NAME,
                        valueType = ValueType.VARIABLE,
                        isRequired = false,
                        slot = slot++
                    ))
                }

                var param = 0
                if (!isStatic || methodName == "<init>") {
                    parameters.add(SingleParameter(
                        description = LocalizedText.Data(emptyMap(), TextValue(className)),
                        name = Translator.localName(param++),
                        valueType = ValueType.ANY,
                        isRequired = false,
                        slot = slot++
                    ))
                }
                for (type in argumentTypes) {
                    parameters.add(SingleParameter(
                        description = LocalizedText.Data(emptyMap(), TextValue(type.className)),
                        name = Translator.localName(param++),
                        valueType = if (type.internalName == "justmc/Variable") ValueType.VARIABLE else ValueType.ANY,
                        isRequired = false,
                        slot = slot++
                    ))
                }

                val functionName = Translator.methodName(className, methodName, methodDesc)

                handlers.add(FunctionTrigger(
                    justOperations,
                    functionName,
                    parameters,
                    LocalizedText(LocalizedText.Data(emptyMap(), TextValue("${className.substringAfterLast('/')}.$methodName"))),
                    LocalizedText(LocalizedText.Data(emptyMap(), TextValue(functionName)))
                ))
            }
            onInit.add(JustOperation(
                "set_variable_create_map_from_values", mapOf(
                    "variable" to Translator.classInstance(className),
                    "keys" to ArrayValue(), // TODO
                    "values" to ArrayValue()
                ))
            )
        }
        if (onInit.isNotEmpty()) {
            handlers.add(0, EventTrigger(onInit, "world_start"))
        }
        return Handlers(handlers)
    }
}