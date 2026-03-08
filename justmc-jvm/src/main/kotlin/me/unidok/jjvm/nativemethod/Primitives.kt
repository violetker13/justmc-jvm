package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.TranslateException
import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.OperationResult
import me.unidok.jjvm.operand.LoadStringConstant
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.LocationValue
import me.unidok.justcode.value.MapValue
import me.unidok.justcode.value.NumberValue
import me.unidok.justcode.value.TextValue

object Primitives {
    private fun Operand.getString(context: TranslationContext): String =
        (this as? LoadStringConstant)?.value ?: this.translate(context, null).toString()

    fun register() {
        NativeMethods.register("justmc/Text.plain(Ljava/lang/String;)Ljustmc/Text;") { method, context ->
            TextValue(method.args[0].getString(context), TextValue.Parsing.PLAIN)
        }
        NativeMethods.register("justmc/Text.legacy(Ljava/lang/String;)Ljustmc/Text;") { method, context ->
            TextValue(method.args[0].getString(context), TextValue.Parsing.LEGACY)
        }
        NativeMethods.register("justmc/Text.mini(Ljava/lang/String;)Ljustmc/Text;") { method, context ->
            TextValue(method.args[0].getString(context), TextValue.Parsing.MINIMESSAGE)
        }
        NativeMethods.register("justmc/Text.json(Ljava/lang/String;)Ljustmc/Text;") { method, context ->
            TextValue(method.args[0].getString(context), TextValue.Parsing.JSON)
        }
        NativeMethods.register("justmc/ListPrimitive.empty()Ljustmc/ListPrimitive;") { method, context ->
            ArrayValue(emptyList())
        }
        NativeMethods.register("justmc/ListPrimitive.of([Ljava/lang/Object;)Ljustmc/ListPrimitive;") { method, context ->
            method.args[0].translate(context, null) as? ArrayValue
                ?: throw TranslateException("Cannot create list primitive from non const array")
        }
        NativeMethods.register("justmc/MapPrimitive.empty()Ljustmc/MapPrimitive;") { method, context ->
            MapValue(emptyMap())
        }
        NativeMethods.register("justmc/MapPrimitive.of([Ljustmc/Pair;)Ljustmc/MapPrimitive;") { method, context ->
            val pairs = method.args[0].translate(context, null) as? ArrayValue
                ?: throw TranslateException("Cannot create map primitive from non const pair array")
            MapValue(
                pairs.values.associateTo(LinkedHashMap(pairs.values.size)) {
                    val pair = (it as ArrayValue).values
                    pair[0] to pair[1]
                }
            )
        }
        NativeMethods.register("justmc/Location.of(DDDFF)Ljustmc/Location;") { method, context ->
            val args = method.args
            val x = args[0].translate(context, null)
            val y = args[1].translate(context, null)
            val z = args[2].translate(context, null)
            val yaw = args[3].translate(context, null)
            val pitch = args[4].translate(context, null)
            run {
                return@register LocationValue(
                    (x as? NumberValue)?.number ?: return@run,
                    (y as? NumberValue)?.number ?: return@run,
                    (z as? NumberValue)?.number ?: return@run,
                    (yaw as? NumberValue)?.number ?: return@run,
                    (pitch as? NumberValue)?.number ?: return@run
                )
            }
            val variable = context.tempVar()
            context.addOperation(JustOperation("set_variable_set_all_coordinates", mapOf(
                "variable" to variable,
                "x" to x,
                "y" to y,
                "z" to z,
                "yaw" to yaw,
                "pitch" to pitch
            )))
            variable
        }
    }
}