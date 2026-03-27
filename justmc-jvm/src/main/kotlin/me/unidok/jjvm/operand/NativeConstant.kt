package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class NativeConstant(
    @JvmField val value: String
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        throw IllegalStateException("NativeConstant cannot be represented as Value")
    }

    override fun toString(): String = "NativeConstant(value=$value)"
}