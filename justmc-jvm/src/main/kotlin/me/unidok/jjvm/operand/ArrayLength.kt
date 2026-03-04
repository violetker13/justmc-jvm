package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class ArrayLength(
    @JvmField val array: Operand
) : SummaryOperand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = variable ?: context.tempVar()
        context.addOperation(JustOperation(
            "set_variable_get_list_length", mapOf(
                "variable" to variable,
                "list" to Translator.instance(array.translate(context, null))
            )
        ))
        return variable
    }

    override fun toString(): String = "ArrayLength(array=$array)"
}