package me.unidok.jjvm

import me.unidok.jjvm.model.JJVMConfig
import me.unidok.jjvm.operand.LoadFromLocal
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.Debugger
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
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodNode
import java.util.jar.JarFile
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.collections.set


/**
 * Сильное связывание:
 * считается, что наш jar больше ничем не расширяется и нигде не используется;
 * благодаря этому мы можем:
 * девиртуализировать вызов методов,
 * сделать экземпляр списком, а не словарём (индексируем поля),
 * встраивать код конструкторов,
 * встраивать короткие методы,
 * оптимизировать алгоритм instanceof
 *
 * Слабое связывание:
 * считается, что наш jar может быть использован другими jar,
 * поэтому он должен иметь более строгую структуру с переиспользованием;
 * из-за этого всё выше перечисленное не будет работать.
 *
 *
 *
 * Пре-обработка:
 * Создание массива операций и индексация меток
 * Узнавание всех своих классов и т.д.
 * TODO Инлайн переменные (класс Variable, Player)
 *
 * Обработка:
 * Генерация кода(?) и ветвлений
 *
 */

class JarTranslator(
    val jarPath: String,
    val config: JJVMConfig
) {
    val finalTypes = HashSet<String>()
    val classes = HashMap<String, SourceClass>()
    val strings = ArrayList<String>()
    val types = ArrayList<Type>()
    val onInit = ArrayList<JustOperation>()

    fun isFinalClass(name: String) = finalTypes.contains(name)

    fun getMaxClassFields(name: String): Int {
        return classes[name]!!.maxFields
    }

    fun findClass(name: String): SourceClass? {
        return classes[name]
    }

    fun translate(): Handlers {

        val debug = config.debug
        val independent = config.independent
        val exclude = config.exclude

        // Чтение JAR
        JarFile(jarPath).use { jarFile ->
            var options = ClassReader.SKIP_FRAMES
            if (!config.skipJarDebug) options += ClassReader.SKIP_DEBUG
            for (entry in jarFile.entries()) {
                val entryName = entry.name
                if (exclude.any { entryName.startsWith(it) }) continue
                if (entryName.endsWith(".class")) {
                    val classNode = ClassNode()
                    ClassReader(jarFile.getInputStream(entry)).accept(classNode, options) // InputStream закрывается сама
                    val clazz = SourceClass(this, classNode)
                    val isFinal = classNode.access and Opcodes.ACC_FINAL != 0
                    if (isFinal) {
                        finalTypes.add(classNode.name)
                    }
                    val className = classNode.name
                    val eventId = classNode.getAnnotation(Annotations.EVENT)?.get("id")
                    if (eventId != null) {
                        if (!isFinal) throw TranslateException("Event class $className")
                    }
                    val methods = HashMap<String, SourceMethod>()
                    for (methodNode in classNode.methods) {
                        val desc = methodNode.desc
                        val method = SourceMethod(clazz, methodNode)
                        Type.getArgumentTypes(desc).forEachIndexed { index, type ->
                            method.resolvedTypes.put(LoadFromLocal(index), type)
                        }
                        methods.put("$className.${methodNode.name}$desc", method)
                    }
                    classes.put(className, SourceClass(this, classNode))
                }
            }
        }

        // Генерация промежуточного представления
        for ((className, clazz) in classes) {
            if (independent) {
                if (!finalTypes.contains(className) && classes.values.all { className != it.node.superName }) {
                    finalTypes.add(className)
                }
            }
            if (debug) Debugger.debugClass(clazz.node)
            for (method in clazz.methods.values) {
                if (debug) Debugger.debugMethod(method.node)
                method.node.instructions.forEachIndexed { index, inst ->
                    when (inst.opcode) {
                        -1 -> if (inst.type == AbstractInsnNode.LABEL) {
                            method.labelIndexes[(inst as LabelNode).label] = index
                        }
                        Opcodes.GOTO -> {
                            method.gotoIndexes[(inst as JumpInsnNode).label.label] = index
                        }
                    }
                }
                BytecodeTranslator(method, method.node.instructions.iterator()).translate(method.operations)
                if (debug) Debugger.debugIR(method)
            }
        }

        val handlers = ArrayList<Trigger>()
        handlers.add(EventTrigger(onInit, "world_start"))
        for ((className, clazz) in classes) {
            for (method in clazz.methods.values) {
                val methodNode = method.node
                val context = TranslationContext(method)
                method.translateOperations(context)
                val justOperations = context.operations

                val methodName = methodNode.name
                if (methodName == "<clinit>") {
                    onInit.addAll(justOperations)
                    continue
                }

                val methodDesc = methodNode.desc
                val methodAccess = methodNode.access
                val isStatic = methodAccess and Opcodes.ACC_STATIC != 0
                val argumentTypes = Type.getArgumentTypes(methodDesc)
                if (isStatic) {
                    val eventHandler = methodNode.getAnnotation(Annotations.EVENT_HANDLER)
                    if (eventHandler != null) {
                        val eventName = eventHandler["id"]
                        if (eventName == null) {
                            if (argumentTypes.size != 1) {
                                throw TranslateException("EventHandler is expecting only one parameter")
                            }
                            val internalName = argumentTypes[0].internalName
                            val event = findClass(internalName)?.node
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

                val functionName = "$className.$methodName$methodDesc"

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
        return Handlers(handlers)
    }
}