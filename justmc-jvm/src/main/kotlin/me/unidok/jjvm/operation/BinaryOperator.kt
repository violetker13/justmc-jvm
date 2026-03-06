package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.EnumValue
import me.unidok.justcode.value.NumberValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class BinaryOperator(
    @JvmField val type: Type,
    @JvmField val operand1: Operand,
    @JvmField val operand2: Operand
) : OperationWithResult {
    enum class Type {
        REM, SHL, SHR, USHR, AND, OR, XOR
    }

    private companion object {
        val LEFT_SHIFT = EnumValue("LEFT_SHIFT")
        val RIGHT_SHIFT = EnumValue("RIGHT_SHIFT")
        val UNSIGNED_RIGHT_SHIFT = EnumValue("UNSIGNED_RIGHT_SHIFT")
        val AND = EnumValue("AND")
        val OR = EnumValue("OR")
        val XOR = EnumValue("XOR")
        val NOT = EnumValue("NOT")
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = variable ?: context.tempVar()
        val operand1 = operand1.translate(context, variable)
        val operand2 = operand2.translate(context, null)
        context.addOperation(when (type) {
            Type.REM -> JustOperation(
                "set_variable_remainder", mapOf(
                    "variable" to variable,
                    "dividend" to operand1,
                    "divisor" to operand2
                )
            )
            Type.SHL -> JustOperation(
                "set_variable_bitwise_operation", mapOf(
                    "variable" to variable,
                    "operand1" to operand1,
                    "operand2" to operand2,
                    "operator" to LEFT_SHIFT
                )
            )
            Type.SHR -> JustOperation(
                "set_variable_bitwise_operation", mapOf(
                    "variable" to variable,
                    "operand1" to operand1,
                    "operand2" to operand2,
                    "operator" to RIGHT_SHIFT
                )
            )
            Type.USHR -> JustOperation(
                "set_variable_bitwise_operation", mapOf(
                    "variable" to variable,
                    "operand1" to operand1,
                    "operand2" to operand2,
                    "operator" to UNSIGNED_RIGHT_SHIFT
                )
            )
            Type.AND -> JustOperation(
                "set_variable_bitwise_operation", mapOf(
                    "variable" to variable,
                    "operand1" to operand1,
                    "operand2" to operand2,
                    "operator" to AND
                )
            )
            Type.OR -> JustOperation(
                "set_variable_bitwise_operation", mapOf(
                    "variable" to variable,
                    "operand1" to operand1,
                    "operand2" to operand2,
                    "operator" to OR
                )
            )
            Type.XOR -> {
                if (operand2 is NumberValue && operand2.number == -1.0) {
                    JustOperation("set_variable_bitwise_operation", mapOf(
                        "variable" to variable,
                        "operand1" to operand1,
                        "operator" to NOT
                    ))
                } else {
                    JustOperation("set_variable_bitwise_operation", mapOf(
                        "variable" to variable,
                        "operand1" to operand1,
                        "operand2" to operand2,
                        "operator" to XOR
                    ))
                }
            }
        })
        return variable
    }

    override fun toString(): String = "BinaryOperator(type=$type, operand1=$operand1, operand2=$operand2)"
}