package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

interface OperationWithResult : Operation {
    fun translate(context: TranslationContext, variable: Variable?): Value

    override fun translate(context: TranslationContext) {
        translate(context, null)
    }
}