package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.operation.InlineMethod
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

fun interface NativeMethod {
    fun invoke(method: InlineMethod, context: TranslationContext, variable: Variable?): Value?
}