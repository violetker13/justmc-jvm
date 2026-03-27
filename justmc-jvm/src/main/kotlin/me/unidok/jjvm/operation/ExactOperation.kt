package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.appendObject

class ExactOperation(
    @JvmField val operation: JustOperation
) : Operation() {
    override fun translate(context: TranslationContext) {
        context.addOperation(operation)
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "Operation",
            "action", operation.action,
            "values", operation.values,
            "operations", operation.operations,
            "selection", operation.selection,
            "conditional", operation.conditional,
            "isInverted", operation.isInverted
        )
    }
}