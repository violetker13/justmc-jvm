package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable
import org.objectweb.asm.Type

class New(
    @JvmField val desc: String
) : OperationWithResult() {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val address = ValueProvider.newInstance(context, variable)
        val sourceClass = context.sourceMethod.sourceClass
        val maxFields = sourceClass.getFields().size
        context.addOperation(JustOperation(
            "set_variable_set_list_value", mapOf(
                "variable" to ValueProvider.instance(address),
                "list" to Values.arrayOfNulls(maxFields + 1),
                "number" to Values.CONST_0,
                "value" to sourceClass.jar.getClassAddress(Type.getObjectType(desc))
            )
        ))
        return address
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.append("New($desc)")
    }
}