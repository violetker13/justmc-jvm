package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class LoadFromLocal(
    @JvmField val local: Int
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return context.localVar(local)
    }

    override fun toString(): String = "LoadFromLocal(local=$local)"
}