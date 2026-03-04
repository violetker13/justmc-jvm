package me.unidok.jjvm

import me.unidok.jjvm.operation.Operation
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.Variable

class TranslationContext(
    val source: MethodContext,
    val operations: MutableList<JustOperation> = ArrayList()
) {
    fun tempVar(): Variable = source.tempVar()
    fun localVar(n: Int): Variable = source.localVar(n)

    fun addOperation(operation: JustOperation) {
        operations.add(operation)
    }

    fun translateChild(operations: List<Operation>): List<JustOperation> {
        val child = TranslationContext(source)
        child.translate(operations)
        return child.operations
    }

    fun translate(operations: List<Operation>) {
        for (operation in operations) operation.translate(this)
    }
}