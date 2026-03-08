package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.util.Values

object ClassObject {
    fun register() {
        NativeMethods.registerWithoutResult("java/lang/Object.<init>()V") { _, _ -> }
        NativeMethods.register("java/lang/Object.toString()Ljava/lang/String;") { method, _ ->
            Values.valueOf(method.self!!.toString())
        }
    }
}