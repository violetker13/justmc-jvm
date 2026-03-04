package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Translator

class PutStatic(
    @JvmField val owner: String,
    @JvmField val name: String,
    @JvmField val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        Translator.setVariable(context, Translator.static(owner, name), value)
    }

    override fun toString(): String = "PutStatic(owner=$owner, name=$name, value=$value)"
}