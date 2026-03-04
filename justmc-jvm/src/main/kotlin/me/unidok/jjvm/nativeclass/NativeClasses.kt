package me.unidok.jjvm.nativeclass

import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.MethodsMap
import me.unidok.jjvm.util.NativeMethod

object NativeClasses {
    private val methods = HashMap<String, MethodsMap>()
    private val fields = HashMap<String, Map<String, Operand>>()

    fun register() {
        JavaClasses.register()
        KotlinIntrinsics.register()
        PrimitiveClasses.register()
        UtilClass.register()
        UnsafeClass.register()
    }

    fun registerMethods(owner: String, methods: MethodsMap) {
        this.methods.put(owner, methods)
    }

    fun findMethod(owner: String, name: String, desc: String): NativeMethod? {
        return methods[owner]?.get(name + desc)
    }

    fun registerFields(owner: String, fields: Map<String, Operand>) {
        this.fields.put(owner, fields)
    }

    fun findField(owner: String, name: String): Operand? {
        return fields[owner]?.get(name)
    }
}