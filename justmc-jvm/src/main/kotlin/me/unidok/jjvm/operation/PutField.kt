package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values

class PutField(
    @JvmField val owner: String,
    @JvmField val name: String,
    @JvmField val desc: String,
    @JvmField val address: Operand,
    @JvmField val value: Operand
) : Operation {
    override fun translate(context: TranslationContext) {
        val clazz = context.sourceMethod.sourceClass.jar.findClass(owner)!!
        val instance = ValueProvider.instance(address.translate(context, null))
        context.addOperation(JustOperation(
            "set_variable_set_list_value", mapOf(
                "variable" to instance,
                "list" to instance,
                "number" to Values.valueOf(clazz.getFields().indexOf(name)),
                "value" to value.translate(context, null)
            )
        ))
    }

    override fun toString(): String = "PutField(owner=$owner, name=$name, value=$value)"
}