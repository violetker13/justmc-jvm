package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.jjvm.util.appendObject
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class GetField(
    @JvmField val owner: String,
    @JvmField val name: String,
    @JvmField val desc: String,
    @JvmField val address: Operand
) : OperationWithResult() {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = variable ?: context.tempVar()
        val clazz = context.sourceMethod.sourceClass.jar.findClass(owner)!!
        val instance = ValueProvider.instance(address.translate(context, null))
        context.addOperation(JustOperation(
            "set_variable_get_list_value", mapOf(
                "variable" to variable,
                "list" to instance,
                "number" to Values.valueOf(clazz.getFields().indexOf(name))
            )
        ))
        return variable
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "GetField",
            "owner", owner,
            "name", name,
            "desc", desc,
            "address", address
        )
    }
}