package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.JustOperation

class IfBranch(
    @JvmField val type: Type,
    @JvmField val operand1: Operand,
    @JvmField val operand2: Operand,
    @JvmField val operations: List<Operation>,
    @JvmField val otherwise: List<Operation>? = null
) : Operation {
    enum class Type {
        EQ, NE, LT, GE, GT, LE;

        fun inv() = when (this) {
            EQ -> NE
            NE -> EQ
            LT -> GE
            GE -> LT
            GT -> LE
            LE -> GT
        }
    }

    override val length: Int
        get() {
            var total = 0
            operations.forEach { total += it.length }
            otherwise?.forEach { total += it.length }
            return total
        }

    override fun translate(context: TranslationContext) {
        val operand1 = operand1.translate(context, null)
        val operand2 = operand2.translate(context, null)
        val operations = context.translateChild(operations)
        val values = mapOf(
            "value" to operand1,
            "compare" to operand2
        )
        val action = when (type) {
            Type.EQ -> "if_variable_equals"
            Type.NE -> "if_variable_not_equals"
            Type.LT -> "if_variable_less"
            Type.GE -> "if_variable_greater_or_equals"
            Type.GT -> "if_variable_greater"
            Type.LE -> "if_variable_less_or_equals"
        }
        context.addOperation(JustOperation(action, values, operations))
        otherwise?.let {
            context.addOperation(JustOperation(
                "else",
                emptyMap(),
                context.translateChild(it)
            ))
        }
    }
}