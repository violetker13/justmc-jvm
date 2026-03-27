package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.OperationResult
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.appendObject
import me.unidok.justcode.value.*

class PluralOperator(
    @JvmField val type: Type,
    @JvmField val deepOperand: Operand,
    @JvmField val simpleOperand: Operand
) : OperationWithResult() {
    enum class Type {
        ADD, SUB, MUL, IDIV, FDIV
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val values = ArrayList<Value>()
        values.add(simpleOperand.translate(context, null))
        val type = type
        var deepOperand = (deepOperand as? OperationResult)?.operation
        while (deepOperand is PluralOperator && deepOperand.type == type) {
            values.add(deepOperand.simpleOperand.translate(context, null))
            deepOperand = (deepOperand.deepOperand as? OperationResult)?.operation
        }
        val variable = variable ?: context.tempVar()
        if (deepOperand == null) {
            values.add(this.deepOperand.translate(context, variable))
        } else {
            values.add(deepOperand.translate(context, variable))
        }
        if ((type == Type.ADD || type == Type.SUB) && values.size == 2 && values[1] == variable) {
            val value = values[0]
            val action = if (type == Type.ADD) "increment" else "decrement"
            context.addOperation(
                if (value is NumberValue && value.number == 1.0) JustOperation(
                    "set_variable_$action", mapOf(
                        "variable" to variable
                    )
                ) else JustOperation(
                    "set_variable_$action", mapOf(
                        "variable" to variable,
                        "value" to value
                    )
                )
            )
            return variable
        }
        val value = ArrayValue(values.asReversed())
        context.addOperation(when (type) {
            Type.ADD -> JustOperation(
                "set_variable_add", mapOf(
                    "variable" to variable,
                    "value" to value
                )
            )
            Type.SUB -> JustOperation(
                "set_variable_subtract", mapOf(
                    "variable" to variable,
                    "value" to value
                )
            )
            Type.MUL -> JustOperation(
                "set_variable_multiply", mapOf(
                    "variable" to variable,
                    "value" to value
                )
            )
            Type.IDIV -> JustOperation(
                "set_variable_divide", mapOf(
                    "variable" to variable,
                    "value" to value,
                    "division_mode" to EnumValue("FLOOR")
                )
            )
            Type.FDIV -> JustOperation(
                "set_variable_divide", mapOf(
                    "variable" to variable,
                    "value" to value
                )
            )
        })
        return variable
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "PluralOperator",
            "deepOperand", deepOperand,
            "simpleOperand", simpleOperand
        )
    }
}