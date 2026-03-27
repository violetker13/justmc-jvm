package me.unidok.jjvm.nativemethod

import me.unidok.jjvm.operand.DoubleConst
import me.unidok.jjvm.operand.OperationResult
import me.unidok.jjvm.operation.NewArray
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.jjvm.util.getArray
import me.unidok.jjvm.util.getString
import me.unidok.jjvm.util.simplify
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.LocationValue
import me.unidok.justcode.value.MapValue
import me.unidok.justcode.value.NumberValue
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

object Primitives {
    fun register() {
        NativeMethods.register("justmc/NumberPrimitive.of(I)Ljustmc/NumberPrimitive;") { method, context, variable ->
            ValueProvider.setVariable(context, variable, method.args[0])
        }
        NativeMethods.register("justmc/Text.plain(Ljava/lang/String;)Ljustmc/Text;") { method, context ->
            println("TEXT PLAIN ${method.args[0]}")
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
        NativeMethods.register("justmc/ListPrimitive.empty()Ljustmc/ListPrimitive;") { _, _ -> Values.EMPTY_ARRAY }
        NativeMethods.registerEarly("justmc/ListPrimitive.of([Ljustmc/Primitive;)Ljustmc/ListPrimitive;") { self, args, method ->
            (args[0].simplify(method.inlineVariables) as? OperationResult)?.takeIf { it.operation is NewArray }
        }
//        NativeMethods.register("justmc/ListPrimitive.of([Ljustmc/Primitive;)Ljustmc/ListPrimitive;") { method, context, variable ->
//            val arg = method.args[0]
//            arg.getArray(context.sourceMethod)
//                ?.map { it.translate(context, null) }
//                ?.let { ValueProvider.setVariable(context, variable, ArrayValue(it))  }
//                ?: ValueProvider.instance(arg.translate(context, variable))
//        }
        NativeMethods.register("justmc/MapPrimitive.empty()Ljustmc/MapPrimitive;") { _, _ -> Values.EMPTY_MAP }
        NativeMethods.register("justmc/MapPrimitive.of([Ljustmc/Pair;)Ljustmc/MapPrimitive;") { method, context, variable ->
            val arg = method.args[0]
            val pairs = arg.getArray(context.sourceMethod)
            if (pairs != null) {
                if (variable == null) run {
                    return@register MapValue(pairs.associate {
                        val pair = it.also {println("PAIR $it")}.getArray(context.sourceMethod).also { println("PAIR ARRAY $it") } ?: return@run
                        pair[0].translate(context, null) to pair[1].translate(context, null)
                    })
                }
                val result = variable ?: context.tempVar()
                val keys = ArrayList<Value>()
                val values = ArrayList<Value>()
                for (pair in pairs) {
                    val constPair = pair.getArray(context.sourceMethod)
                    if (constPair != null) {
                        keys.add(constPair[0].translate(context, null))
                        values.add(constPair[1].translate(context, null))
                    } else {
                        val pair = pair.translate(context, null)
                        val first = context.tempVar()
                        val second = context.tempVar()
                        context.addOperation(JustOperation(
                            "set_variable_get_list_value", mapOf(
                                "variable" to first,
                                "list" to pair,
                                "number" to Values.CONST_0
                            )
                        ))
                        context.addOperation(JustOperation(
                            "set_variable_get_list_value", mapOf(
                                "variable" to second,
                                "list" to pair,
                                "number" to Values.CONST_1
                            )
                        ))
                        keys.add(first)
                        values.add(second)
                    }
                }
                context.addOperation(JustOperation(
                    "set_variable_create_map_from_values", mapOf(
                        "variable" to result,
                        "keys" to ArrayValue(keys),
                        "values" to ArrayValue(values)
                    )
                ))
                return@register result
            }
            null
//            else {
//                val result = variable ?: context.tempVar()
//                context.addOperation(JustOperation(
//                    "set_variable_create_map", mapOf(
//                        "variable" to result
//                    )
//                ))
//                val pair = context.tempVar()
//                val first = context.tempVar()
//                val second = context.tempVar()
//                context.addOperation(JustOperation(
//                    "repeat_for_each_in_list",
//                    mapOf(
//                        "value_variable" to pair,
//                        "list" to arg.translate(context, null)
//                    ),
//                    listOf(
//                        JustOperation(
//                            "set_variable_get_list_value", mapOf(
//                                "variable" to first,
//                                "list" to pair,
//                                "number" to Values.CONST_0
//                            )
//                        ),
//                        JustOperation(
//                            "set_variable_get_list_value", mapOf(
//                                "variable" to second,
//                                "list" to pair,
//                                "number" to Values.CONST_1
//                            )
//                        ),
//                        JustOperation(
//                            "set_variable_set_map_value", mapOf(
//                                "variable" to result,
//                                "map" to result,
//                                "key" to first,
//                                "value" to second
//                            )
//                        )
//                    )
//                ))
//                result
//            }
        }
        NativeMethods.register("justmc/Location.of(DDDFF)Ljustmc/Location;") { method, context ->
            val args = method.args
            val sourceMethod = context.sourceMethod
            LocationValue(
                (args[0].simplify(sourceMethod.inlineVariables) as? DoubleConst)?.value ?: return@register null,
                (args[1].simplify(sourceMethod.inlineVariables) as? DoubleConst)?.value ?: return@register null,
                (args[2].simplify(sourceMethod.inlineVariables) as? DoubleConst)?.value ?: return@register null,
                (args[3].simplify(sourceMethod.inlineVariables) as? DoubleConst)?.value ?: return@register null,
                (args[4].simplify(sourceMethod.inlineVariables) as? DoubleConst)?.value ?: return@register null
            )
        }
        NativeMethods.register("justmc/Variable.save(Ljava/lang/String;)Ljustmc/Variable;") { method, context ->
            Variable(method.args[0].getString(context), Variable.Scope.SAVE)
        }
        NativeMethods.register("justmc/Variable.game(Ljava/lang/String;)Ljustmc/Variable;") { method, context ->
            Variable(method.args[0].getString(context), Variable.Scope.GAME)
        }
        NativeMethods.register("justmc/Variable.local(Ljava/lang/String;)Ljustmc/Variable;") { method, context ->
            Variable(method.args[0].getString(context), Variable.Scope.LOCAL)
        }
        NativeMethods.register("justmc/Variable.line(Ljava/lang/String;)Ljustmc/Variable;") { method, context ->
            Variable(method.args[0].getString(context), Variable.Scope.LINE)
        }
        NativeMethods.register("justmc/Variable.temp()Ljustmc/Variable;") { method, context ->
            context.provider.tempVar()
        }
        NativeMethods.register("justmc/Variable.result()Ljustmc/Variable;") { method, context ->
            context.provider.resultVar()
        }
    }
}