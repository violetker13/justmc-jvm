package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.appendObject

class ReturnValue(
    val value: Operand
) : Operation() {
    override fun translate(context: TranslationContext) {
        value.translate(context, context.provider.returnVariable)
        //TODO context.addOperation(context.provider.returnOperation)
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "ReturnValue",
            "value", value
        )
    }
}