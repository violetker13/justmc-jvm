package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.NumberValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class NewArray(
    @JvmField val size: Operand
) : OperationWithResult {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = Translator.newInstance(context, variable)
        val size = size.translate(context, null)
        if (size is NumberValue) {
            val zero = Values.CONST_0
            context.addOperation(JustOperation(
                "set_variable_create_list", mapOf(
                    "variable" to Translator.instance(variable),
                    "values" to ArrayValue(List(size.number.toInt()) { zero })
                ))
            )
        } else {
            context.addOperation(JustOperation(
                "set_variable_create_list", mapOf(
                    "variable" to Translator.instance(variable),
                ))
            )
            context.addOperation(JustOperation(
                "repeat_multi_times", mapOf(
                    "amount" to size
                ), listOf(
                    JustOperation(
                        "set_variable_append_value", mapOf(
                            "variable" to Translator.instance(variable),
                            "values" to Values.CONST_0
                        )
                    )
                ))
            )
        }
        return variable
    }

    override fun toString(): String = "NewArray(size=$size)"
}