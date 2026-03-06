package me.unidok.jjvm

import me.unidok.jjvm.operation.Operation
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator.localName
import me.unidok.justcode.value.Variable

data class TranslationContext(
    val sourceMethod: SourceMethod,
    val iterator: Iterator<Operation>,
    val operations: MutableList<JustOperation> = ArrayList(),
    var tempVars: Int = 0,
    val varOffset: Int = 0,
) {
    fun tempVar(): Variable {
        return Variable("t${tempVars++}", Variable.Scope.LINE)
    }

    fun localVar(n: Int): Variable {
        return Variable(localName(n + varOffset), Variable.Scope.LINE)
    }

    fun addOperation(operation: JustOperation) {
        operations.add(operation)
    }

    fun translateChild(operations: List<Operation>): List<JustOperation> {
        val result = ArrayList<JustOperation>()

        return result
    }

    fun translate(operations: List<Operation>): List<JustOperation> {
        val iterator = operations.iterator()
        val context = copy(sourceMethod, iterator)
        while (iterator.hasNext()) iterator.next().translate(context)
        return context.operations
    }
}