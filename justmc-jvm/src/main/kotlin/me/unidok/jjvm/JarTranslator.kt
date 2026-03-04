package me.unidok.jjvm

import me.unidok.jjvm.model.JJVMConfig
import me.unidok.jjvm.operand.LoadFromLocal
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.Translator
import me.unidok.jjvm.util.getAnnotation
import me.unidok.justcode.Handlers
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import java.util.jar.JarFile


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

object JarTranslator {
    fun translate(jarPath: String, config: JJVMConfig): Handlers {
        val finalTypes = HashSet<String>()
        val classByName = HashMap<String, ClassNode>()
        val classMethodsMaps = HashMap<ClassNode, Map<String, MethodNode>>()
        val contexts = HashMap<MethodNode, MethodContext>()
        val source = SourceContext(config, finalTypes, classByName, classMethodsMaps, contexts)
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
                    val clazz = ClassNode()
                    ClassReader(jarFile.getInputStream(entry)).accept(clazz, options) // InputStream закрывается сама
                    val isFinal = clazz.access and Opcodes.ACC_FINAL != 0
                    if (isFinal) {
                        finalTypes.add(clazz.name)
                    }
                    val className = clazz.name
                    val eventId = clazz.getAnnotation(Annotations.EVENT)?.get("id")
                    if (eventId != null) {
                        if (!isFinal) throw TranslateException("Event class $className")
                    }
                    val methodsMap = HashMap<String, MethodNode>()

                    for (method in clazz.methods) {
                        val desc = method.desc
                        val methodContext = MethodContext(source, clazz, method)
                        contexts[method] = methodContext
                        Type.getArgumentTypes(desc).forEachIndexed { index, type ->
                            methodContext.resolvedTypes.put(LoadFromLocal(index), type)
                        }
                        methodsMap.put(Translator.methodName(className, method.name, desc), method)
                    }
                    classByName.put(className, clazz)
                    classMethodsMaps.put(clazz, methodsMap)
                }
            }
        }

        // Генерация промежуточного представления
        for (clazz in classByName.values) {
            if (independent) {
                val name = clazz.name
                if (!finalTypes.contains(name) && classByName.values.all { name != it.superName }) {
                    finalTypes.add(name)
                }
            }
            if (debug) Debugger.debugClass(clazz)
            for (method in clazz.methods) {
                if (debug) Debugger.debugMethod(method)
                val methodContext = contexts[method]!!
                methodContext.translateBytecode()
                if (debug) Debugger.debugIR(methodContext)
            }
        }

        return source.writeHandlers()
    }
}