package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.util.JustOperation

class NonAffectOperation(
    @JvmField val operation: JustOperation
) : Operation {
    override fun translate(context: TranslationContext) {
        context.addOperation(operation)
    }

    override fun toString(): String = "$operation"
}