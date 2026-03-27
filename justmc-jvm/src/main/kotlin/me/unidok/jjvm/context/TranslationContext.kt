package me.unidok.jjvm.context

import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operation.Operation
import me.unidok.jjvm.operation.OperationWithResult
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class TranslationContext(
    val sourceMethod: SourceMethod,
    val iterator: Iterator<Operation>,
    val destination: MutableList<JustOperation> = ArrayList(),
    val provider: ValueProvider = ValueProvider(),
    val translated: MutableMap<OperationWithResult, Value> = HashMap(),
) {
    fun tempVar(): Variable = provider.tempVar()

    fun addOperation(operation: JustOperation) {
        destination.add(operation)
    }

    fun translateChild(operations: List<Operation>): List<JustOperation> {
        val result = ArrayList<JustOperation>()
        copy(
            iterator = operations.iterator(),
            destination = result
        ).translate()
        return result
    }

    fun translate() {
        val iterator = iterator
        while (iterator.hasNext()) iterator.next().translate(this)
    }
}