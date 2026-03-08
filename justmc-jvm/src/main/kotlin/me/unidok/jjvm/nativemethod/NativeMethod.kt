package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operation.InvokeNativeMethod
import me.unidok.justcode.value.Value

fun interface NativeMethod {
    fun invoke(method: InvokeNativeMethod, context: TranslationContext): Value?
}