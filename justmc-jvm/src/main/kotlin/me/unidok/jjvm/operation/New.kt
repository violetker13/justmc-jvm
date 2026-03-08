package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class New(
    @JvmField val desc: String
) : OperationWithResult {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = ValueProvider.newInstance(context, variable)
        val maxFields = context.sourceMethod.sourceClass.getFields().size
        context.addOperation(JustOperation(
            "set_variable_create_list", mapOf(
                "variable" to ValueProvider.instance(variable),
                "values" to ArrayValue(buildList(maxFields) {
                    TextValue(desc)
                    var i = 0
                    while (++i < maxFields) add(Values.CONST_0)
                    // TODO Optimize init fields
                })
            ))
        )
        return variable
    }

    override fun toString(): String = "New(desc=$desc)"
}