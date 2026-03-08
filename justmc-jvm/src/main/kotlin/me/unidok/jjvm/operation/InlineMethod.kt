package me.unidok.jjvm.operation

import me.unidok.jjvm.SourceMethod
import me.unidok.jjvm.TranslateException
import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.isAnnotated
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class InlineMethod(
    val method: SourceMethod,
    val initOperations: List<Operation>
) : OperationWithResult {
    override val length: Int
        get() = initOperations.sumOf { it.length } + method.getLength()

    fun translate0(context: TranslationContext, variable: Variable?) {
        val provider = context.provider
        context.copy(
            iterator = iterator {
                yieldAll(initOperations)
                yieldAll(method.operations)
            },
            provider = provider.copy(
                returnVariable = variable ?: ValueProvider.defaultReturnVariable,
                varOffset = provider.varOffset + method.node.maxLocals
            )
        ).translate()
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = variable ?: context.tempVar()
        translate0(context, variable)
        return variable
    }

    override fun translate(context: TranslationContext) {
        translate0(context, null)
    }
}