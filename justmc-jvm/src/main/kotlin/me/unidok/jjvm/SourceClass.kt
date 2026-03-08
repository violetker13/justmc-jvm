package me.unidok.jjvm

import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.trigger.Trigger
import org.objectweb.asm.tree.ClassNode

class SourceClass(
    val jar: JarTranslator,
    val node: ClassNode,
    val methods: Map<String, SourceMethod>
) {
    private var fields: List<String>? = null

    fun getFields(): List<String> = fields ?: node.fields.mapTo(
        jar.findClass(node.superName)
            ?.let { ArrayList(it.getFields()) }
            ?: ArrayList()
    ) { it.name }.also { fields = it }

}