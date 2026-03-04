package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.EnumValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class UnaryOperator(
    @JvmField val type: Type,
    @JvmField val operand: Operand
) : SummaryOperand {
    enum class Type {
        NEG, F2L, L2I, I2B, I2C, I2S
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = variable ?: context.tempVar()
        val operand = operand.translate(context, variable)
        context.addOperation(when (type) {
            Type.NEG -> JustOperation("set_variable_multiply", mapOf(
                "variable" to variable,
                "number" to ArrayValue(operand, DynamicConstant.CONST_M1.value)
            ))
            Type.F2L -> JustOperation("set_variable_round", mapOf(
                "variable" to variable,
                "number" to operand,
                "round_type" to EnumValue("FLOOR")
            ))
            else -> TODO()
        })
        return variable
    }

    override fun toString(): String = "UnaryOperator(type=$type, operand=$operand)"
}