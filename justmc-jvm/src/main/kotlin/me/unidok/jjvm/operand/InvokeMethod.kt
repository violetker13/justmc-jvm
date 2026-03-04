package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operation.Operation
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.justcode.value.*
import org.objectweb.asm.Type

/*
	invoke dynamic:
	 desc: '(I)Ljava/lang/String;';
	 name: 'makeConcatWithConstants';
	 bsm: 'java/lang/invoke/StringConcatFactory.makeConcatWithConstants(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; (6)';
          bsm name: 'makeConcatWithConstants';
          bsm desc: '(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;';
          bsm owher: 'java/lang/invoke/StringConcatFactory';
          bsm tag: '6';
          bsm interface: 'false';
	 bsmArgs: '[Ljava.lang.Object;@45c8e616'
 */
class InvokeMethod(
    @JvmField val owner: String,
    @JvmField val name: String,
    @JvmField val desc: String,
    @JvmField val devirtualized: Boolean,
    @JvmField val self: Operand?,
    @JvmField val args: Array<out Operand>
) : SummaryOperand, Operation {
    private fun translate0(context: TranslationContext, variable: Variable?) {
        val defaultFunctionName = TextValue(Translator.methodName(owner, name, desc))
        val functionName: Value
        val args = args
        val self = self?.translate(context, null)
        if (self != null && !devirtualized) {
            functionName = context.tempVar()
            context.addOperation(
                JustOperation(
                    "set_variable_get_map_value", mapOf(
                        "variable" to functionName,
                        "map" to Translator.classInstance(self),
                        "key" to TextValue(name + desc),
                        "default_value" to defaultFunctionName
                    )
                )
            )
        } else {
            functionName = defaultFunctionName
        }
        context.addOperation(
            JustOperation(
                "call_function", mapOf(
                    "function_name" to functionName,
                    "args" to MapValue(buildMap {
                        if (Type.getReturnType(desc) != Type.VOID_TYPE) {
                            put(Translator.RETURN_KEY, variable ?: EmptyValue)
                        }
                        var index = 0
                        if (self != null) {
                            put(Translator.argumentKey(0), self)
                            index = 1
                        }
                        for (arg in args) {
                            put(Translator.argumentKey(index++), arg.translate(context, null))
                        }
                    })
                )
            )
        )
    }

    override fun translate(context: TranslationContext) {
        translate0(context, null)
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val variable = variable ?: context.tempVar()
        translate0(context, variable)
        return variable
    }

    override fun toString(): String = "InvokeMethod(owner=$owner, name=$name, desc=$desc, self=$self, args=${args.joinToString(", ", "[", "]")})"
}