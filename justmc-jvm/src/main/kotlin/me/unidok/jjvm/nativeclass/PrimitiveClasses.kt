package me.unidok.jjvm.nativeclass

import me.unidok.jjvm.operand.DynamicConstant
import me.unidok.justcode.value.LocationValue
import me.unidok.justcode.value.NumberValue
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.number

object PrimitiveClasses {
    fun register() {
        NativeClasses.registerMethods("justmc/Location", hashMapOf(
            "of(DDDFF)Ljustmc/Location;" to {
                val x = arg(0)
                val y = arg(0)
                val z = arg(0)
                val yaw = arg(0)
                val pitch = arg(0)
                if (x is DynamicConstant && y is DynamicConstant && z is DynamicConstant && yaw is DynamicConstant && pitch is DynamicConstant) {
                    DynamicConstant.valueOf(LocationValue(x.number, y.number, z.number, yaw.number, pitch.number))
                } else {
                    val variable = source.tempVar()
                    translateFuture {
                        addOperation(JustOperation("set_variable_set_all_coordinates", mapOf(
                            "variable" to variable,
                            "x" to x.translate(this, null),
                            "y" to y.translate(this, null),
                            "z" to z.translate(this, null),
                            "yaw" to yaw.translate(this, null),
                            "pitch" to pitch.translate(this, null)
                        )))
                    }
                    DynamicConstant.valueOf(variable)
                }
            }
        ))
    }
}