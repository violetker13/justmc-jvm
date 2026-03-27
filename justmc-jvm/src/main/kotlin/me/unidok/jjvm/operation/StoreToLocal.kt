package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.OperationResult
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.appendObject
import org.objectweb.asm.Type

class StoreToLocal(
    @JvmField val local: Int,
    @JvmField val value: Operand
) : Operation() {
    override fun translate(context: TranslationContext) {
        ValueProvider.setVariable(context, context.provider.localVar(local), value)
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "StoreToLocal",
            "local", local,
            "value", value
        )
    }
}