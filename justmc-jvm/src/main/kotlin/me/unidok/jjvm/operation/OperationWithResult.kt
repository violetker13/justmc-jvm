package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

abstract class OperationWithResult : Operation() {
    abstract fun translate(context: TranslationContext, variable: Variable?): Value

    override fun translate(context: TranslationContext) {
        context.translated.getOrPut(this) { translate(context, null) }
    }
}