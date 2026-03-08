package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class GetStatic(
    @JvmField val owner: String,
    @JvmField val name: String
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return ValueProvider.staticVar(owner, name)
    }

    override fun toString(): String = "GetStatic(owner=$owner, name=$name)"
}