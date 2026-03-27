package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class IntConst(
    @JvmField val value: Int
) : Operand {
    companion object {
        val cache = Array(256) { IntConst(it - 128) }

        fun valueOf(value: Int): IntConst {
            if (value >= -128 && value <= 127) return cache[value + 128]
            return IntConst(value)
        }

        val CONST_M1 = valueOf(-1)
        val CONST_0 = valueOf(0)
        val CONST_1 = valueOf(1)
        val CONST_2 = valueOf(2)
        val CONST_3 = valueOf(3)
        val CONST_4 = valueOf(4)
        val CONST_5 = valueOf(5)
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return Values.valueOf(value)
    }

    override fun equals(other: Any?): Boolean = other is IntConst && other.value == value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "IntConst($value)"
}