package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.nativemethod.NativeMethod
import me.unidok.jjvm.operand.Operand
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class InvokeNativeMethod(
    @JvmField val method: NativeMethod,
    @JvmField val self: Operand?,
    @JvmField val args: Array<out Operand>
) : OperationWithResult {
    override fun translate(context: TranslationContext) {
        method.invoke(this, context)
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return ValueProvider.setVariable(context, variable, method.invoke(this, context)!!)
    }
}