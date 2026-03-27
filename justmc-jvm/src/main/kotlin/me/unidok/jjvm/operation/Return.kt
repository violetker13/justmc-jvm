package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext

object Return : Operation() {
    override fun translate(context: TranslationContext) {
        //TODO context.addOperation(context.provider.returnOperation)
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.append("Return")
    }
}