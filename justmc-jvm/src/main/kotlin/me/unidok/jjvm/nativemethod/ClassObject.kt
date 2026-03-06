package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.util.Values

object ClassObject {
    fun register() {
        NativeMethods.register("java/lang/Object.<init>()V") { _, _ ->
            null
        }
        NativeMethods.register("java/lang/Object.toString()Ljava/lang/String;") { method, _ ->
            Values.valueOf(method.self!!.toString())
        }
    }
}