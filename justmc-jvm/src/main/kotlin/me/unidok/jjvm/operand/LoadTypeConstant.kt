package me.unidok.jjvm.operand

import me.unidok.jjvm.context.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable
import org.objectweb.asm.Type

data class LoadTypeConstant(
    val value: Type
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return context.sourceMethod.sourceClass.jar.getClassAddress(value)
    }
}