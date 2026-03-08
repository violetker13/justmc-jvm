package me.unidok.jjvm.util

import me.unidok.jjvm.operation.IfBranch
import me.unidok.jjvm.operation.LoopBranch
import me.unidok.jjvm.operation.Operation
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

typealias JustOperation = me.unidok.justcode.operation.Operation

//fun Operand.requireConstString(message: String? = null): String {
//    if (this is NativeConstant) return value
//    if (this is DynamicConstant) return value.toString()
//    throw TranslateException(message ?: "$javaClass cannot be represented as constant string")
//}

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