package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.DynamicConstant
import me.unidok.jjvm.util.JustOperation

class Increment(
    @JvmField val local: Int,
    @JvmField val value: Int
) : Operation {
    override fun translate(context: TranslationContext) {
        val variable = context.localVar(local)
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
                    "number" to DynamicConstant.valueOf(value).value
                )
            )
            else -> JustOperation(
                "set_variable_decrement", mapOf(
                    "variable" to variable,
                    "number" to DynamicConstant.valueOf(value).value
                )
            )
        })
    }

    override fun toString(): String = "Increment(local=$local, value=$value)"
}