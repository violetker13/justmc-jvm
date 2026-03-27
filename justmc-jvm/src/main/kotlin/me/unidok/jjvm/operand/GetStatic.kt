package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class GetStatic(
    @JvmField val owner: String,
    @JvmField val name: String
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return ValueProvider.staticVar(owner, name)
    }

    override fun toString(): String = "GetStatic(owner=$owner, name=$name)"
}