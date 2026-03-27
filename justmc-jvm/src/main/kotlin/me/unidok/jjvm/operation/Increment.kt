package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.jjvm.util.appendObject

class Increment(
    @JvmField val local: Int,
    @JvmField val value: Int
) : Operation() {
    override fun translate(context: TranslationContext) {
        val variable = context.provider.localVar(local)
        val value = value
        context.addOperation(when {
            value == 1 -> JustOperation(
                "set_variable_increment", mapOf(
                    "variable" to variable
                )
            )
            value == -1 -> JustOperation(
                "set_variable_decrement", mapOf(
                    "variable" to variable
                )
            )
            value >= 0 -> JustOperation(
                "set_variable_increment", mapOf(
                    "variable" to variable,
                    "number" to Values.valueOf(value)
                )
            )
            else -> JustOperation(
                "set_variable_decrement", mapOf(
                    "variable" to variable,
                    "number" to Values.valueOf(value)
                )
            )
        })
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "Increment",
            "local", local,
            "value", value
        )
    }
}