package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.getString
import me.unidok.justcode.value.MapValue
import me.unidok.justcode.value.TextValue

object ClassUnsafe {
    private val regex = Regex("[a-z_]+")

    fun register() {
        NativeMethods.registerWithoutResult("justmc/Unsafe.operation(Ljava/lang/String;Ljustmc/MapPrimitive;)V") { method, context ->
            println("ID ${method.args[0]}")
            val action = method.args[0].getString(context.sourceMethod)
                ?: throw IllegalStateException("Operation ID must be const string")
            val args = method.args[1].also { println("ARGS $it") }.translate(context, null).also { println("ARGS TRANSLATED $it") } as? MapValue
                ?: throw IllegalStateException("Operation arguments must be map primitive")
            println("args ${args.values}")
            val values = args.values.mapKeys { (it.key as TextValue).text }
            context.addOperation(JustOperation(action, values))
        }

        val nothingToDo = EarlyNativeMethod { self, args, method -> args[0] }
        arrayOf(
            "justmc/Unsafe.asBoolean(Ljustmc/Primitive;)Z",
            "justmc/Unsafe.asByte(Ljustmc/Primitive;)B",
            "justmc/Unsafe.asShort(Ljustmc/Primitive;)S",
            "justmc/Unsafe.asChar(Ljustmc/Primitive;)C",
            "justmc/Unsafe.asInt(Ljustmc/Primitive;)I",
            "justmc/Unsafe.asLong(Ljustmc/Primitive;)J",
            "justmc/Unsafe.asFloat(Ljustmc/Primitive;)F",
            "justmc/Unsafe.asDouble(Ljustmc/Primitive;)D",
            "justmc/Unsafe.cast(Ljava/lang/Object;)Ljava/lang/Object;",
            "justmc/Unsafe.asAddress(Ljava/lang/Object;)I",
            "justmc/Unsafe.asObject(I)Ljava/lang/Object;"
        ).forEach {
            NativeMethods.registerEarly(it, nothingToDo)
        }

        NativeMethods.register("justmc/Unsafe.getInstance(I)Ljustmc/Variable;") { method, context ->
            ValueProvider.instance(method.args[0].translate(context, null))
        }
    }
}