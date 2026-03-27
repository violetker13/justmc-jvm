package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.appendObject
import me.unidok.justcode.value.Variable

class StoreToConstantPool(
    val key: String,
    val value: Operand
) : Operation() {
    override fun translate(context: TranslationContext) {
        ValueProvider.setVariable(context, Variable(key), value)
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "StoreToConstantPool",
            "key", key,
            "value", value
        )
    }
}