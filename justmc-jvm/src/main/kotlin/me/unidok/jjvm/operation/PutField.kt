package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.justcode.value.TextValue

class PutField(
    @JvmField val owner: Operand,
    @JvmField val name: String,
    @JvmField val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        val owner = Translator.instance(owner.translate(context, null))
        context.addOperation(JustOperation(
            "set_variable_set_map_value", mapOf(
                "variable" to owner,
                "map" to owner,
                "key" to TextValue(name),
                "value" to value.translate(context, null)
            )
        ))
    }

    override fun toString(): String = "PutField(owner=$owner, name=$name, value=$value)"
}