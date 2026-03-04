package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class OperandResult(
    val operand: SummaryOperand
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return context.source.translatedOperands[operand]!!
    }

    override fun toString(): String = "OperandResult(operand=$operand)"
}