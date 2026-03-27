package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.appendObject

class PutStatic(
    @JvmField val owner: String,
    @JvmField val name: String,
    @JvmField val value: Operand
) : Operation() {
    override fun translate(context: TranslationContext) {
        ValueProvider.setVariable(context, ValueProvider.staticVar(owner, name), value)
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "PutStatic",
            "field", "$owner.$name",
            "value", value
        )
    }
}