package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.appendObject

class LoopBranch(
    @JvmField val operations: List<Operation>
) : Operation() {
    override val length: Int
        get() = operations.sumOf { it.length }

    override fun translate(context: TranslationContext) {
        context.addOperation(JustOperation(
            "repeat_forever",
            emptyMap(),
            context.translateChild(operations)
        ))
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "LoopBranch",
            "operations", operations
        )
    }
}