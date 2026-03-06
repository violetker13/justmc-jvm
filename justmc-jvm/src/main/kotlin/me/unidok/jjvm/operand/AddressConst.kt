package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class AddressConst(
    val value: Int
) : Operand {
    companion object {
        val CONST_NULL = AddressConst(0)
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return Values.valueOf(value)
    }
}