package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.util.Debugger
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class LoadFromLocal(
    @JvmField val local: Int
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        context.sourceMethod.inlineVariables[local]?.let { return it.translate(context, variable) }
        return ValueProvider.setVariable(context, variable, context.provider.localVar(local))
    }
}