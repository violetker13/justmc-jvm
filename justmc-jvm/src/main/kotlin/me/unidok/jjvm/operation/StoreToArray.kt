package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.JustOperation

class StoreToArray(
    @JvmField val array: Operand,
    @JvmField val index: Operand,
    @JvmField val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        val array = ValueProvider.instance(array.translate(context, null))
        context.addOperation(JustOperation(
            "set_variable_set_list_value", mapOf(
                "variable" to array,
                "list" to array,
                "number" to index.translate(context, null),
                "value" to value.translate(context, null)
            )
        ))
    }

    override fun toString(): String = "StoreToArray(array=$array, index=$index, value=$value)"
}