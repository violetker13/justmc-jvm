package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.TranslateException
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.MapValue
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value

object ClassUnsafe {
    private val regex = Regex("[a-z_]+")

    private fun Value.requireId(): String {
        val value = this as? TextValue ?: throw TranslateException("Argument must be const string")
        if (!regex.matches(value.text)) throw TranslateException("Argument must matches with $regex")
        return value.text
    }

    fun register() {
        NativeMethods.register("justmc/Unsafe.operation(Ljustmc/Text;Ljustmc/CopyableMap;)V") { method, context ->
            val action = method.args[0].translate(context, null).requireId()
            val args = method.args[1].translate(context, null) as? MapValue
                ?: throw TranslateException("Operation arguments must be const copyable map")
            val values = args.values.mapKeys { it.key.requireId() }
            context.addOperation(JustOperation(action, values))
            null
        }
        val nothingToDo: NativeMethod = { method, context ->
            method.args[0].translate(context, null)
        }
        arrayOf(
            "justmc/Unsafe.asBoolean(Ljustmc/Primitive;)Z" to nothingToDo,
            "justmc/Unsafe.asByte(Ljustmc/Primitive;)B" to nothingToDo,
            "justmc/Unsafe.asShort(Ljustmc/Primitive;)S" to nothingToDo,
            "justmc/Unsafe.asChar(Ljustmc/Primitive;)C" to nothingToDo,
            "justmc/Unsafe.asInt(Ljustmc/Primitive;)I" to nothingToDo,
            "justmc/Unsafe.asLong(Ljustmc/Primitive;)J" to nothingToDo,
            "justmc/Unsafe.asFloat(Ljustmc/Primitive;)F" to nothingToDo,
            "justmc/Unsafe.asDouble(Ljustmc/Primitive;)D" to nothingToDo,
            "justmc/Unsafe.asString(Ljava/lang/Object;)Ljava/lang/String;" to nothingToDo,
        )
    }
}