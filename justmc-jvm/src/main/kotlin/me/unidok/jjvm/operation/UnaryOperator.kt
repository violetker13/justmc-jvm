package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.jjvm.util.appendObject
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.EnumValue
import me.unidok.justcode.value.NumberValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class UnaryOperator(
    @JvmField val type: Type,
    @JvmField val operand: Operand
) : OperationWithResult() {
    enum class Type {
        NEG, F2L, L2I, I2B, I2C, I2S
    }

    private companion object {
        val INT_MIN = NumberValue(Int.MIN_VALUE.toDouble())
        val INT_MAX = NumberValue(Int.MAX_VALUE.toDouble())
        val BYTE_MIN = NumberValue(Byte.MIN_VALUE.toDouble())
        val BYTE_MAX = NumberValue(Byte.MAX_VALUE.toDouble())
        val CHAR_MIN = NumberValue(Char.MIN_VALUE.code.toDouble())
        val CHAR_MAX = NumberValue(Char.MAX_VALUE.code.toDouble())
        val SHORT_MIN = NumberValue(Short.MIN_VALUE.toDouble())
        val SHORT_MAX = NumberValue(Short.MAX_VALUE.toDouble())
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = variable ?: context.tempVar()
        val operand = operand.translate(context, variable)
        context.addOperation(when (type) {
            Type.NEG -> JustOperation("set_variable_multiply", mapOf(
                "variable" to variable,
                "number" to ArrayValue(operand, Values.CONST_M1)
            ))
            Type.F2L -> JustOperation("set_variable_round", mapOf(
                "variable" to variable,
                "number" to operand,
                "round_type" to EnumValue("FLOOR")
            ))
            Type.L2I -> JustOperation("set_variable_warp", mapOf(
                "variable" to variable,
                "number" to operand,
                "min" to INT_MIN,
                "max" to INT_MAX
            ))
            Type.I2B -> JustOperation("set_variable_warp", mapOf(
                "variable" to variable,
                "number" to operand,
                "min" to BYTE_MIN,
                "max" to BYTE_MAX
            ))
            Type.I2C -> JustOperation("set_variable_warp", mapOf(
                "variable" to variable,
                "number" to operand,
                "min" to CHAR_MIN,
                "max" to CHAR_MAX
            ))
            Type.I2S -> JustOperation("set_variable_warp", mapOf(
                "variable" to variable,
                "number" to operand,
                "min" to SHORT_MIN,
                "max" to SHORT_MAX
            ))
        })
        return variable
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "UnaryOperator",
            "type", type,
            "operand", operand
        )
    }
}