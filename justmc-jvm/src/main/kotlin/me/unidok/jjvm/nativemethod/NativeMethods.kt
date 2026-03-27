package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.context.SourceMethod
import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operation.InlineMethod
import me.unidok.jjvm.operation.Operation
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

object NativeMethods {
    private val earlyMethods = HashMap<String, EarlyNativeMethod>()
    private val methods = HashMap<String, NativeMethod>()
    private val fields = HashMap<String, Map<String, Operand>>()

    fun register() {
        ClassUnsafe.register()
        Primitives.register()
    }

    fun register(name: String, method: NativeMethod) {
        methods.put(name, method)
    }

    inline fun registerWithoutResult(name: String, crossinline block: (InlineMethod, TranslationContext) -> Unit) {
        register(name) { method, context, variable ->
            block(method, context)
            null
        }
    }

    inline fun register(name: String, crossinline block: (InlineMethod, TranslationContext) -> Value?) {
        register(name) { method, context, variable ->
            block(method, context)?.let { ValueProvider.setVariable(context, variable, it) }
        }
    }

    fun registerEarly(name: String, method: EarlyNativeMethod) {
        earlyMethods.put(name, method)
    }

    fun findMethod(name: String): NativeMethod? = methods[name]

    fun findEarlyMethod(name: String): EarlyNativeMethod? = earlyMethods[name]

    fun registerFields(owner: String, fields: Map<String, Operand>) {
        this.fields.put(owner, fields)
    }

    fun findField(owner: String, name: String): Operand? {
        return fields[owner]?.get(name)
    }
}