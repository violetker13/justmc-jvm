package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class FloatConst(
    val value: Double
) : Operand {
    companion object {
        val CONST_0 = FloatConst(0.0)
        val CONST_1 = FloatConst(1.0)
        val CONST_2 = FloatConst(2.0)
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return Values.valueOf(value)
    }
}