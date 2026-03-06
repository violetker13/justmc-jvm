package me.unidok.jjvm.nativemethod

import me.unidok.justcode.value.LocationValue
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.EnumValue
import me.unidok.justcode.value.NumberValue
import me.unidok.justcode.value.TextValue

object Primitives {
    fun register() {
        NativeMethods.register("justmc/Text.of(Ljava/lang/String;Ljustmc/TextParsing;)Ljustmc/Text;") { method, context ->
            val value = method.args[0].translate(context, null)
            val parsing = method.args[1].translate(context, null)
            if (parsing is EnumValue) {
                val parsing = TextValue.Parsing.valueOf(parsing.name)
                if (value is TextValue) {
                    if (value.parsing == parsing) return@register value
                    return@register TextValue(value.text, parsing)
                }
                return@register TextValue(value.toString(), parsing)
            }
            val result = context.tempVar()
            context.addOperation(JustOperation("set_variable_change_component_parsing", mapOf(
                "variable" to result,
                "component" to value,
                "parsing" to parsing
            )))
            result
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