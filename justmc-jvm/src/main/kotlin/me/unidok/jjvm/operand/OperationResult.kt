package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.operation.OperationWithResult
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class OperationResult(
    val operation: OperationWithResult
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
//        return context.sourceMethod.translated.getOrPut(operation) {
//            operation.translate(context, variable)
//        }
        println("OPERATION RESULT $operation")
        return operation.translate(context, variable)
    }

    override fun toString(): String = operation.toString()
}