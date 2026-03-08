package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.operand.Operand

class StoreToLocal(
    @JvmField val local: Int,
    @JvmField val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        ValueProvider.setVariable(context, context.provider.localVar(local), value)
    }

    override fun toString(): String = "StoreToLocal(local=$local, value=$value)"
}