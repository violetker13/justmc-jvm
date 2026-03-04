package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.SummaryOperand

class GetOperationResult(
    val operand: SummaryOperand
) : Operation {
    override fun translate(context: TranslationContext) {
        val variable = context.tempVar()
        operand.translate(context, variable)
        context.source.translatedOperands[operand] = variable
    }
}