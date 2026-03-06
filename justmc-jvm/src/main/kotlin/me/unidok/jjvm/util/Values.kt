package me.unidok.jjvm.util

import me.unidok.justcode.value.GameValue
import me.unidok.justcode.value.NumberValue
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value

object Values {
    private val biCache = Array(256) { NumberValue((it - 128).toDouble()) }
    private val dcCache = HashMap<Any, Value>()
    private val gameValues = HashMap<String, GameValue>()

    fun valueOf(value: Int): Value {
        if (value >= -128 && value <= 127) return biCache[value + 128]
        return dcCache.getOrPut(value) { NumberValue(value.toDouble()) }
    }

    fun valueOf(value: Float): Value {
        val integer = value.toInt()
        if (value - integer == 0f) return valueOf(integer)
        return dcCache.getOrPut(value) { NumberValue(value.toDouble()) }
    }

    fun valueOf(value: Long): Value {
        if (value >= -128 && value <= 127) return biCache[value.toInt() + 128]
        return dcCache.getOrPut(value) { NumberValue(value.toDouble()) }
    }

    fun valueOf(value: Double): Value {
        val integer = value.toInt()
        if (value - integer == 0.0) return valueOf(integer)
        return dcCache.getOrPut(value) { NumberValue(value) }
    }

    fun valueOf(value: String): Value {
        return dcCache.getOrPut(value) { TextValue(value) }
    }

    fun gameValueOf(name: String): Value {
        return gameValues.getOrPut(name) { GameValue(name) }
    }

    val CONST_M1 = valueOf(-1)
    val CONST_0 = valueOf(0)
    val CONST_1 = valueOf(1)
    val CONST_2 = valueOf(2)
    val CONST_3 = valueOf(3)
    val CONST_4 = valueOf(4)
    val CONST_5 = valueOf(5)
}

