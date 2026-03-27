package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data object NullConst : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return Values.CONST_0
    }
}