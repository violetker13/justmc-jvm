package me.unidok.jjvm.nativeclass

import me.unidok.jjvm.operand.DynamicConstant
import me.unidok.jjvm.operand.NativeConstant
import me.unidok.jjvm.operand.New
import me.unidok.jjvm.operand.OperandResult
import me.unidok.jjvm.operation.GetOperationResult
import me.unidok.jjvm.operation.PutField
import me.unidok.jjvm.util.NativeMethod
import me.unidok.jjvm.util.Translator
import me.unidok.justcode.value.TextValue

object JavaClasses {
//    private val primitives = hashSetOf(
//        "java/lang/Boolean",
//        "java/lang/Byte",
//        "java/lang/Short",
//        "java/lang/Character",
//        "java/lang/Integer",
//        "java/lang/Long",
//        "java/lang/Float",
//        "java/lang/Double",
//        "java/lang/String",
//        "justmc/Block",
//        "justmc/CopyableList",
//        "justmc/CopyableMap",
//        "justmc/Item",
//        "justmc/Location",
//        "justmc/Potion",
//        "justmc/Text",
//        "justmc/Variable",
//        "justmc/Vector",
//    )

    private fun newInstance(desc: String): NativeMethod = {
        val instance = New(desc)
        val instanceOp = OperandResult(instance)
        addOperation(GetOperationResult(instance))
        addOperation(PutField(instanceOp, "value", arg(0)))
        instanceOp
    }

    fun register() {
        NativeClasses.registerMethods("java/lang/Object", hashMapOf(
            "<init>()V" to {
                null
            },
            "toString()Ljava/lang/String;" to {
                when (val value = self) {
                    is NativeConstant -> value
                    is DynamicConstant -> DynamicConstant.valueOf(value.toString())
                    else -> null
                }
            },
        ))
        NativeClasses.registerMethods("java/lang/Boolean", hashMapOf(
            "valueOf(Z)Ljava/lang/Boolean;" to newInstance("java/lang/Boolean")
        ))
        NativeClasses.registerMethods("java/lang/Byte", hashMapOf(
            "valueOf(B)Ljava/lang/Byte;" to newInstance("java/lang/Byte")
        ))
        NativeClasses.registerMethods("java/lang/Character", hashMapOf(
            "valueOf(C)Ljava/lang/Character;" to newInstance("java/lang/Character")
        ))
        NativeClasses.registerMethods("java/lang/Short", hashMapOf(
            "valueOf(S)Ljava/lang/Short;" to newInstance("java/lang/Short")
        ))
        NativeClasses.registerMethods("java/lang/Integer", hashMapOf(
            "valueOf(I)Ljava/lang/Integer;" to newInstance("java/lang/Integer")
        ))
        NativeClasses.registerMethods("java/lang/Long", hashMapOf(
            "valueOf(J)Ljava/lang/Long;" to newInstance("java/lang/Long")
        ))
        NativeClasses.registerMethods("java/lang/Float", hashMapOf(
            "valueOf(F)Ljava/lang/Float;" to newInstance("java/lang/Float")
        ))
        NativeClasses.registerMethods("java/lang/Double", hashMapOf(
            "valueOf(D)Ljava/lang/Double;" to newInstance("java/lang/Double")
        ))
    }
}