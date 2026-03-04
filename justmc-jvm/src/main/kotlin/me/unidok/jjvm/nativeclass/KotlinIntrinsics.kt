package me.unidok.jjvm.nativeclass

import me.unidok.jjvm.operand.DynamicConstant
import me.unidok.jjvm.operation.IfBranch
import me.unidok.jjvm.operation.NonAffectOperation
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.requireConstString
import me.unidok.justcode.value.EnumValue
import me.unidok.justcode.value.TextValue

object KotlinIntrinsics {
    fun register() {
        NativeClasses.registerMethods("kotlin/jvm/internal/Intrinsics", hashMapOf(
            "checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V" to {
                val paramName = arg(1).requireConstString()
                val className = source.clazz.name
                val methodName = source.method.name
                addOperation(IfBranch(
                    IfBranch.Type.EQ,
                    arg(0),
                    DynamicConstant.CONST_0,
                    listOf(NonAffectOperation(JustOperation(
                        "control_call_exception", mapOf(
                            "type" to EnumValue("ERROR"),
                            "message" to TextValue("Parameter specified as non-null is null: method $className.$methodName, parameter $paramName")
                        )
                    )))
                ))
                null
            },
        ))
    }
}