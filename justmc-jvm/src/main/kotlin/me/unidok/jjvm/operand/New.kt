package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class New(
    @JvmField val desc: String
) : SummaryOperand {
    private companion object {
        val CLASS = TextValue("class")
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = Translator.newInstance(context, variable)
        context.addOperation(JustOperation(
            "set_variable_create_map_from_values", mapOf(
                "variable" to Translator.instance(variable),
                "keys" to CLASS,
                "values" to TextValue(desc)
            ))
        )
        return variable
    }

    override fun toString(): String = "New(desc=$desc)"
}