package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class IntegerConst(
    @JvmField val value: Long
) : Operand {
    constructor(value: Int) : this(value.toLong())

    companion object {
        @JvmField val cache = Array(256) { IntegerConst(it - 128) }

        @JvmStatic
        fun valueOf(value: Int): IntegerConst {
            if (value >= -128 && value <= 127) return cache[value + 128]
            return IntegerConst(value)
        }

        @JvmField val CONST_M1 = cache[127]
        @JvmField val CONST_0 = cache[128]
        @JvmField val CONST_1 = cache[129]
        @JvmField val CONST_2 = cache[130]
        @JvmField val CONST_3 = cache[131]
        @JvmField val CONST_4 = cache[132]
        @JvmField val CONST_5 = cache[133]
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return Values.valueOf(value)
    }
}