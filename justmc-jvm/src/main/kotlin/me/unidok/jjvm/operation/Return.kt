package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext

object Return : Operation {
    override fun translate(context: TranslationContext) {
        context.addOperation(context.provider.returnOperation)
    }
}