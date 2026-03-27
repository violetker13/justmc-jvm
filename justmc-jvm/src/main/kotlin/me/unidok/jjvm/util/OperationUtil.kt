package me.unidok.jjvm.util

import me.unidok.jjvm.context.SourceMethod
import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.nativemethod.EarlyNativeMethod
import me.unidok.jjvm.operand.LoadFromLocal
import me.unidok.jjvm.operand.LoadStringConstant
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.OperationResult
import me.unidok.jjvm.operation.InlineMethod
import me.unidok.jjvm.operation.NewArray
import me.unidok.jjvm.operation.ReturnValue
import org.objectweb.asm.tree.AnnotationNode

typealias JustOperation = me.unidok.justcode.operation.Operation

fun Operand.simplify(inlineVariables: Map<Int, Operand>): Operand {
    println("simplify $this inlines = ${inlineVariables}")
    when (this) {
        is LoadFromLocal -> inlineVariables[local]?.let { return it.simplify(inlineVariables).also {println("SIMPLIFIED1 $it") } }
        is OperationResult -> {
            val operation = this.operation
            when (operation) {
                is InlineMethod -> {
                    val method = operation.method
                    val op = method.operations.firstOrNull()
                    println("INLINE METHOD '${method.fullName}' FIRST OP $op")
                    if (op is ReturnValue) {
                        return op.value.simplify(
                            HashMap<Int, Operand>().apply {
                                var index = 0
                                operation.self?.let { put(index++, it.simplify(inlineVariables)) }
                                for (arg in operation.args) {
                                    val arg = arg.simplify(inlineVariables)
                                    println("INLINE ARG $index = $arg")
                                    put(index++, arg)
                                }
                            }
                        ).also {println("SIMPLIFIED2 $it") }
                    }
                }
            }
        }
    }
    return this.also {println("SIMPLIFIED3(no) $it") }
}

fun Operand.getString(method: SourceMethod): String? =
    (simplify(method.inlineVariables) as? LoadStringConstant)?.value

fun Operand.getString(context: TranslationContext): String =
    getString(context.sourceMethod) ?: translate(context, null).toString()

fun Operand.getArray(method: SourceMethod): List<Operand>? =
    ((simplify(method.inlineVariables) as? OperationResult)?.operation as? NewArray)?.let {
        method.constArrays[it].also { println("constARRAY ${it}") }
    }

fun String.replaceOverride(): String = replace("override/", "")

object Annotations {
    const val INLINE = "Ljustmc/annotation/Inline;"
    const val EVENT = "Ljustmc/annotation/Event;"
    const val EVENT_HANDLER = "Ljustmc/annotation/EventHandler;"
    const val FUNCTION_NAME = "Ljustmc/annotation/FunctionName;"
}

fun List<AnnotationNode>?.isAnnotated(annotation: String): Boolean =
    this != null && any { it.desc == annotation }

fun List<AnnotationNode>?.getAnnotation(annotation: String): Map<String, Any>? {
    val values = this?.find { it.desc == annotation }?.values ?: return null
    val size = values.size
    val result = HashMap<String, Any>(size / 2)
    var index = 0
    while (index < size) {
        result.put((values[index++] as String), values[index++])
    }
    return result
}