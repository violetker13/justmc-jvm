package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operation.OperationWithResult
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class OperationResult(
    val operation: OperationWithResult
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return context.sourceMethod.translated[operation]!!
    }

    override fun toString(): String = "OperandResult(operation=$operation)"
}