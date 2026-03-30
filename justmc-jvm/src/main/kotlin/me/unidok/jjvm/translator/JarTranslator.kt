package me.unidok.jjvm.translator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.unidok.jjvm.context.SourceClass
import me.unidok.jjvm.context.SourceMethod
import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.model.JJVMConfig
import me.unidok.jjvm.operand.LoadFromLocal
import me.unidok.jjvm.operand.NativeValue
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.OperationResult
import me.unidok.jjvm.operation.*
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.justcode.Handlers
import me.unidok.justcode.trigger.EventTrigger
import me.unidok.justcode.trigger.FunctionTrigger
import me.unidok.justcode.trigger.Trigger
import me.unidok.justcode.value.*
import me.unidok.justcode.value.parameter.Parameter
import me.unidok.justcode.value.parameter.SingleParameter
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.jar.JarFile
import kotlin.collections.get
import kotlin.collections.iterator

class JarTranslator(
    val jarPath: String,
    val config: JJVMConfig
) {
    val finalTypes = HashSet<String>()
    val classes = HashMap<String, SourceClass>()
    val registerNatives = ArrayList<Operation>()
    val dynamicConstants = HashMap<Any, Variable>()

    fun isFinalClass(name: String) = finalTypes.contains(name)

    fun findClass(name: String?): SourceClass? {
        return classes[name]
    }

    fun findMethod(owner: String, name: String, desc: String): SourceMethod? {
        return classes[owner]?.methods[name + desc]
    }

    fun getClassAddress(type: Type): Variable = dynamicConstants[type]
        ?: type.className.let { className ->
            val variable = Variable("$className.class")
            dynamicConstants[type] = variable
            val new = New("java/lang/Class")
            val adr = OperationResult(new)
            val store = StoreToConstantPool(variable.name, adr)
            val putField = PutField("java/lang/Class", "name", "Ljustmc/Text;", adr, NativeValue(Values.valueOf(className)))
            registerNatives.addAll(listOf(new, store, putField))
            variable
        }

    fun translate(): Handlers {
        val debug = config.printDebug
        val exclude = config.exclude
        val excludeUnused = !config.includeUnused

        // Чтение JAR
        JarFile(jarPath).use { jarFile ->
            for (entry in jarFile.entries()) {
                val entryName = entry.name
                if (entryName.endsWith(".class")) {
                    val classNode = ClassNode()
                    ClassReader(jarFile.getInputStream(entry)).accept(classNode, ClassReader.SKIP_FRAMES)

                    val methods = HashMap<String, SourceMethod>()
                    val clazz = SourceClass(this@JarTranslator, classNode, methods)
                    val className = clazz.name

                    if (className == "java/lang/Object") {
                        classNode.superName = null
                    }

                    val isFinal = clazz.access and Opcodes.ACC_FINAL != 0
                    if (isFinal) {
                        finalTypes.add(className)
                    }

                    val eventId = clazz.getAnnotation(Annotations.EVENT)?.get("id")
                    if (eventId != null) {
                        // TODO проверять что конструктор приватный
                        if (!isFinal) throw IllegalStateException("Event class $className must be final")
                    }

                    for (methodNode in classNode.methods) {
                        val method = SourceMethod(clazz, methodNode)
                        val desc = method.desc
                        Type.getArgumentTypes(desc).forEachIndexed { index, type ->
                            method.resolvedTypes.put(LoadFromLocal(index), type)
                        }
                        methods.put(method.fullName, method)
                    }

                    classes.put(className, clazz)
                }
            }
        }

        // Генерация промежуточного представления
        for (clazz in classes.values) {
            if (debug) Debugger.debugClass(clazz.node)
            for (method in clazz.methods.values) {
                if (debug) Debugger.debugMethod(method.node)
                method.translateBytecode()
                if (debug) Debugger.debugIR(method)
            }
        }

//        val jobs = ArrayList<Job>()
//
//        for (clazz in classes.values) {
//            for (method in clazz.methods.values) {
//                jobs.add(launch { method.translateBytecode() })
//            }
//        }
//
//        jobs.joinAll()
//
//        if (debug) {
//            for (clazz in classes.values) {
//                Debugger.debugClass(clazz.node)
//                for (method in clazz.methods.values) {
//                    Debugger.debugMethod(method.node)
//                    Debugger.debugIR(method)
//                }
//            }
//        }






        val handlers = ArrayList<Trigger>()
        val onWorldStart = ArrayList<JustOperation>()
        onWorldStart.add(ValueProvider.setVariable(ValueProvider.maxArrayVariable, Values.MAX_ARRAY))
        handlers.add(EventTrigger(onWorldStart, "world_start"))
        for ((className, clazz) in classes) {
            if (exclude.any { className.startsWith(it) }) continue
            for (method in clazz.methods.values) {
                println("Translating ${method.fullName}")
                val methodName = method.name
                val methodDesc = method.desc

                if (methodName == "<clinit>") {
                    registerNatives.add(InlineMethod(method, null, emptyArray()))
                    continue
                }

                if (excludeUnused && method.calls == 0) continue

                val justOperations = ArrayList<JustOperation>()
                val context = TranslationContext(method, method.operations.iterator(), justOperations)

                try {
                    context.translate()
                } catch (e: Exception) {
                    throw Exception("Translate exception ${clazz.node.sourceFile} ${method.fullName}", e)
                }

                val methodAccess = method.access
                val isStatic = methodAccess and Opcodes.ACC_STATIC != 0
                val argumentTypes = Type.getArgumentTypes(methodDesc)
                if (isStatic) {
                    val eventHandler = method.getAnnotation(Annotations.EVENT_HANDLER)
                    if (eventHandler != null) {
                        val eventName = eventHandler["id"]
                        if (eventName == null) {
                            if (argumentTypes.size != 1) {
                                throw IllegalStateException("EventHandler is expecting only one parameter")
                            }
                            val internalName = argumentTypes[0].internalName
                            val event = findClass(internalName)
                                ?.getAnnotation(Annotations.EVENT)?.get("id")?.toString()
                                ?: throw NullPointerException("Unknown event '$internalName'")
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
                        name = context.provider.returnVariable.name,
                        valueType = ValueType.VARIABLE,
                        isRequired = false,
                        slot = slot++
                    ))
                }

                var param = 0
                if (!isStatic || methodName == "<init>") {
                    parameters.add(SingleParameter(
                        description = LocalizedText.Data(emptyMap(), TextValue(className)),
                        name = context.provider.localVar(param++).name,
                        valueType = ValueType.ANY,
                        isRequired = false,
                        slot = slot++
                    ))
                }
                for (type in argumentTypes) {
                    parameters.add(SingleParameter(
                        description = LocalizedText.Data(emptyMap(), TextValue(type.className)),
                        name = context.provider.localVar(param++).name,
                        valueType = if (type.internalName == "justmc/Variable") ValueType.VARIABLE else ValueType.ANY,
                        isRequired = false,
                        slot = slot++
                    ))
                }

                val functionName = method.getAnnotation(Annotations.FUNCTION_NAME)?.get("name") as? String ?: method.fullName

                handlers.add(FunctionTrigger(
                    justOperations,
                    functionName,
                    parameters,
                    LocalizedText(LocalizedText.Data(emptyMap(), TextValue("${className.substringAfterLast('/')}.$methodName"))),
                    LocalizedText(LocalizedText.Data(emptyMap(), TextValue(functionName)))
                ))
            }
        }
        TranslationContext(
            sourceMethod = classes["java/lang/Class"]!!.methods["java/lang/Class.registerNatives()V"]!!,
            iterator = registerNatives.iterator(),
            destination = onWorldStart,
        ).translate()
        return Handlers(handlers)
    }
}