package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Translator

class StoreToLocal(
    @JvmField val local: Int,
    @JvmField val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        Translator.setVariable(context, context.localVar(local), value)
    }

    override fun toString(): String = "StoreToLocal(local=$local, value=$value)"
}