package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.operand.Operand

class PutStatic(
    @JvmField val owner: String,
    @JvmField val name: String,
    @JvmField val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        ValueProvider.setVariable(context, ValueProvider.staticVar(owner, name), value)
    }

    override fun toString(): String = "PutStatic(owner=$owner, name=$name, value=$value)"
}