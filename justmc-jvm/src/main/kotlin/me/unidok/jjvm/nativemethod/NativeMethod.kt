package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operation.InvokeMethod
import me.unidok.justcode.value.Value

fun interface NativeMethod {
    fun invoke(method: InvokeMethod, context: TranslationContext): Value?
}