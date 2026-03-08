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
        NativeMethods.registerWithoutResult("justmc/Unsafe.operation(Ljustmc/Text;Ljustmc/MapPrimitive;)V") { method, context ->
            val action = method.args[0].translate(context, null).requireId()
            val args = method.args[1].translate(context, null) as? MapValue
                ?: throw TranslateException("Operation arguments must be map primitive")
            val values = args.values.mapKeys { it.key.requireId() }
            context.addOperation(JustOperation(action, values))
        }
        val nothingToDo: NativeMethod = { method, context ->
            method.args[0].translate(context, null)
        }
        arrayOf(
            "justmc/Unsafe.asBoolean(Ljustmc/Primitive;)Z",
            "justmc/Unsafe.asByte(Ljustmc/Primitive;)B",
            "justmc/Unsafe.asShort(Ljustmc/Primitive;)S",
            "justmc/Unsafe.asChar(Ljustmc/Primitive;)C",
            "justmc/Unsafe.asInt(Ljustmc/Primitive;)I",
            "justmc/Unsafe.asLong(Ljustmc/Primitive;)J",
            "justmc/Unsafe.asFloat(Ljustmc/Primitive;)F",
            "justmc/Unsafe.asDouble(Ljustmc/Primitive;)D",
            "justmc/Unsafe.asString(Ljava/lang/Object;)Ljava/lang/String;"
        ).forEach {
            NativeMethods.register(it, nothingToDo)
        }
    }
}