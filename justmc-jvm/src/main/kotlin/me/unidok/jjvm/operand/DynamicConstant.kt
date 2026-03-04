package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslateException
import me.unidok.jjvm.TranslationContext
import me.unidok.justcode.value.*
import org.objectweb.asm.Type

data class DynamicConstant(
    @JvmField val value: Value
) : Operand {
    companion object {
        private val biCache = Array(256) { DynamicConstant(NumberValue((it - 128).toDouble())) }
        private val dcCache = HashMap<Any, DynamicConstant>()
        private val valueCache = HashMap<Value, DynamicConstant>()
        private val gameValues = HashMap<String, DynamicConstant>()

        fun valueOf(value: Int): DynamicConstant {
            if (value >= -128 && value <= 127) return biCache[value + 128]
            return dcCache.getOrPut(value) { DynamicConstant(NumberValue(value.toDouble())) }
        }

        fun valueOf(value: Float): DynamicConstant {
            val integer = value.toInt()
            if (value - integer == 0f) return valueOf(integer)
            return dcCache.getOrPut(value) { DynamicConstant(NumberValue(value.toDouble())) }
        }

        fun valueOf(value: Long): DynamicConstant {
            if (value >= -128 && value <= 127) return biCache[value.toInt() + 128]
            return dcCache.getOrPut(value) { DynamicConstant(NumberValue(value.toDouble())) }
        }

        fun valueOf(value: Double): DynamicConstant {
            val integer = value.toInt()
            if (value - integer == 0.0) return valueOf(integer)
            return dcCache.getOrPut(value) { DynamicConstant(NumberValue(value)) }
        }

        fun valueOf(value: String): DynamicConstant {
            return dcCache.getOrPut(value) { DynamicConstant(TextValue(value)) }
        }

        fun gameValueOf(name: String): DynamicConstant {
            return gameValues.getOrPut(name) { DynamicConstant(GameValue(name)) }
        }

        fun valueOf(value: Value): DynamicConstant {
            return valueCache.getOrPut(value) { DynamicConstant(value) }
        }

        val CONST_M1 = valueOf(-1)
        val CONST_0 = valueOf(0)
        val CONST_1 = valueOf(1)
        val CONST_2 = valueOf(2)
        val CONST_3 = valueOf(3)
        val CONST_4 = valueOf(4)
        val CONST_5 = valueOf(5)

        fun fromAny(obj: Any): DynamicConstant = when (obj) {
            is Int -> valueOf(obj)
            is Float -> valueOf(obj)
            is Long -> valueOf(obj)
            is Double -> valueOf(obj)
            is String -> valueOf(obj)
            is Type -> TODO()
            else -> throw TranslateException("Unknown value ${obj.javaClass} $obj")
        }
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return value
    }

    override fun toString(): String = "$value"
}