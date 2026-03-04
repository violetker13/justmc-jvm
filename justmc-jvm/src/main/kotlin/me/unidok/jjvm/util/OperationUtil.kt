package me.unidok.jjvm.util

import me.unidok.jjvm.MethodContext
import me.unidok.jjvm.TranslateException
import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.DynamicConstant
import me.unidok.jjvm.operand.InvokeMethod
import me.unidok.jjvm.operand.NativeConstant
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operation.NonAffectOperation
import me.unidok.jjvm.operation.Operation
import me.unidok.jjvm.operation.TranslatableFutureOperation
import me.unidok.justcode.value.NumberValue
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

typealias NativeMethod = NativeMethodContext.() -> Operand?
typealias MethodsMap = Map<String, NativeMethod>
typealias JustOperation = me.unidok.justcode.operation.Operation

fun Operand.requireConstString(message: String? = null): String {
    if (this is NativeConstant) return value
    if (this is DynamicConstant) return value.toString()
    throw TranslateException(message ?: "$javaClass cannot be represented as constant string")
}

object Annotations {
    const val INLINE = "Ljustmc/annotation/Inline;"
    const val EVENT = "Ljustmc/annotation/Event;"
    const val EVENT_HANDLER = "Ljustmc/annotation/EventHandler;"
}

fun ClassNode.isAnnotated(annotation: String) = invisibleAnnotations.isAnnotated(annotation)
fun MethodNode.isAnnotated(annotation: String) = invisibleAnnotations.isAnnotated(annotation)
fun List<AnnotationNode>?.isAnnotated(annotation: String) = this != null && any { it.desc == annotation }
fun ClassNode.getAnnotation(annotation: String) = invisibleAnnotations.getAnnotation(annotation)
fun MethodNode.getAnnotation(annotation: String) = invisibleAnnotations.getAnnotation(annotation)
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

fun List<JustOperation>.totalLength(): Int {
    var total = size
    for (operation in this) {
        operation.operations?.let {
            total += it.totalLength()
        }
    }
    return total
}

val DynamicConstant.number: Double get() = (value as NumberValue).number

data class NativeMethodContext(
    val method: InvokeMethod,
    val source: MethodContext,
    val operations: MutableList<Operation>
) {
    fun arg(index: Int): Operand = method.args[index]

    val self get() = method.self

    fun translateFuture(block: TranslationContext.() -> Unit) {
        operations.add(TranslatableFutureOperation(block))
    }

    fun addOperation(operation: JustOperation) {
        operations.add(NonAffectOperation(operation))
    }

    fun addOperation(operation: Operation) {
        operations.add(operation)
    }
}