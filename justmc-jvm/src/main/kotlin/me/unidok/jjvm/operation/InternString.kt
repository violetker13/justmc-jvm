package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class InternString(
    val string: String
) : OperationWithResult {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        context.sourceMethod.sourceClass.jar

    }
}