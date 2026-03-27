package me.unidok.jjvm.context

import me.unidok.jjvm.translator.JarTranslator
import me.unidok.jjvm.util.getAnnotation
import me.unidok.jjvm.util.isAnnotated
import me.unidok.jjvm.util.replaceOverride
import org.objectweb.asm.tree.ClassNode

class SourceClass(
    val jar: JarTranslator,
    val node: ClassNode,
    val methods: Map<String, SourceMethod>
) {
    val name: String = node.name.replaceOverride()
    val access: Int get() = node.access

    fun isAnnotated(annotation: String): Boolean =
        node.invisibleAnnotations.isAnnotated(annotation)

    fun getAnnotation(annotation: String): Map<String, Any>? =
        node.invisibleAnnotations.getAnnotation(annotation)

    private var fields: List<String>? = null

    fun getFields(): List<String> = fields ?: node.fields.mapTo(
        jar.findClass(node.superName)
            ?.let { ArrayList(it.getFields()) }
            ?: ArrayList()
    ) { it.name }.also { fields = it }

}