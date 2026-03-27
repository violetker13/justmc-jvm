package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class DoubleConst(
    val value: Double
) : Operand {
    companion object {
        val CONST_0 = DoubleConst(0.0)
        val CONST_1 = DoubleConst(1.0)
        val CONST_2 = DoubleConst(2.0)
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return Values.valueOf(value)
    }

    override fun equals(other: Any?): Boolean = other is DoubleConst && other.value == value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "DoubleConst($value)"
}