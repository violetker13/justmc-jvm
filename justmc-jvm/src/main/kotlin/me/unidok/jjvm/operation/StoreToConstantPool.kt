package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.justcode.value.Variable

class StoreToConstantPool(
    val key: String,
    val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        ValueProvider.setVariable(context, Variable(key), value)
    }
}