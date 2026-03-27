package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class NativeValue(
    val value: Value
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return value
    }
}