package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operation.InvokeNativeMethod
import me.unidok.justcode.value.Value

object NativeMethods {
    private val methods = HashMap<String, NativeMethod>()
    private val fields = HashMap<String, Map<String, Operand>>()

    fun register() {
        ClassObject.register()
        ClassUnsafe.register()
        Primitives.register()
    }

    fun register(name: String, method: NativeMethod) {
        methods.put(name, method)
    }

    inline fun registerWithoutResult(name: String, crossinline block: (InvokeNativeMethod, TranslationContext) -> Unit) {
        register(name, NativeMethod { method, context ->
            block(method, context)
            null
        })
    }

    inline fun register(name: String, crossinline block: (InvokeNativeMethod, TranslationContext) -> Value) {
        register(name, NativeMethod { method, context ->
            block(method, context)
        })
    }

    fun findMethod(name: String): NativeMethod? = methods[name]

    fun registerFields(owner: String, fields: Map<String, Operand>) {
        this.fields.put(owner, fields)
    }

    fun findField(owner: String, name: String): Operand? {
        return fields[owner]?.get(name)
    }
}