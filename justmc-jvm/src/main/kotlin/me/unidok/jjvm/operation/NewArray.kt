package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.IntConst
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.jjvm.util.appendObject
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class NewArray(
    @JvmField val size: Operand
) : OperationWithResult() {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val address = ValueProvider.newInstance(context, variable)
        val instance = ValueProvider.instance(address)
        val size = size
        if (size is IntConst) {
            val constArray = context.sourceMethod.constArrays[this]
            if (constArray == null) {
                val size = size.value
                if (size == Values.MAX_ARRAY_SIZE) {
                    context.addOperation(
                        ValueProvider.setVariable(instance, ValueProvider.maxArrayVariable)
                    )
                } else if (size <= Values.MAX_ARRAY_CACHE) {
                    context.addOperation(
                        ValueProvider.setVariable(instance, Values.arrayOfNulls(size))
                    )
                } else {
                    context.addOperation(JustOperation(
                        "set_variable_trim_list", mapOf(
                            "variable" to instance,
                            "list" to ValueProvider.maxArrayVariable,
                            "start" to Values.CONST_0,
                            "end" to Values.valueOf(size)
                        )
                    ))
                }
            } else {
                context.addOperation(JustOperation(
                    "set_variable_create_list", mapOf(
                        "variable" to instance,
                        "values" to ArrayValue(constArray.map { it.translate(context, null) })
                    )
                ))
            }
        } else {
            context.addOperation(JustOperation(
                "set_variable_trim_list", mapOf(
                    "variable" to instance,
                    "list" to ValueProvider.maxArrayVariable,
                    "start" to Values.CONST_0,
                    "end" to size.translate(context, null)
                )
            ))
        }
        return address
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "NewArray",
            "size", size
        )
    }
}