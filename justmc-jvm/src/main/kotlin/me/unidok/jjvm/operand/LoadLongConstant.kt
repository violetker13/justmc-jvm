package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operation.NonAffectOperation
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class LoadLongConstant(
    val value: Long
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val jar = context.sourceMethod.sourceClass.jar
        val constants = jar.dynamicConstants
        return constants[value] ?: Variable("${value}L").also { variable ->
            constants[value] = variable
            jar.registerNatives.add(NonAffectOperation(JustOperation(
                "set_variable_convert_text_to_number",
                mapOf(
                    "variable" to variable,
                    "text" to TextValue(value.toString(16)),
                    "radix" to Values.valueOf(16)
                )
            )))
        }
    }
}