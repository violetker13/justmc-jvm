package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class GetField(
    @JvmField val owner: Operand,
    @JvmField val name: String
) : OperationWithResult {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = variable ?: context.tempVar()
        context.addOperation(JustOperation(
            "set_variable_get_map_value", mapOf(
                "variable" to variable,
                "map" to owner.translate(context, variable),
                "key" to TextValue(name)
            )
        ))
        return variable
    }

    override fun toString(): String = "GetField(owner=$owner, name=$name)"
}