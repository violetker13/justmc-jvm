package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand

class SetReturnValue(
    val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        value.translate(context, context.provider.returnVariable)
    }
}