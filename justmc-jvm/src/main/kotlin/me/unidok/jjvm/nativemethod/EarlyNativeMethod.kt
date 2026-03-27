package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.context.SourceMethod
import me.unidok.jjvm.operand.Operand

fun interface EarlyNativeMethod {
    fun invoke(self: Operand?, args: Array<out Operand>, method: SourceMethod): Operand?
}