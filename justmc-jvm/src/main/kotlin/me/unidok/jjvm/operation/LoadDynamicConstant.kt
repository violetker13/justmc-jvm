package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class LoadDynamicConstant(
    val value: Any
) : OperationWithResult {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val jar = context.sourceMethod.sourceClass.jar
        val constants = jar.constants
        var index = constants.indexOf(value)
        if (index == -1) {
            index = constants.size
            constants.add(value)
            jar.onInit
        }
    }
}