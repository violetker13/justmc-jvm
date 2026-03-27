package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.jjvm.util.appendObject
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
    override val owner: String,
    override val name: String,
    override val desc: String,
    val virtual: Boolean,
    val self: Operand?,
    val args: Array<out Operand>
) : MethodOperation() {
    private fun translate0(context: TranslationContext, variable: Variable?) {
        val owner = owner
        val name = name
        val desc = desc
        val sourceMethod = context.sourceMethod
        val jar = sourceMethod.sourceClass.jar
        val args = args
        val self = self?.translate(context, null)
        val functionName: Value
        if (virtual) {
            functionName = context.tempVar()
            context.addOperation(JustOperation(
                "set_variable_get_map_value", mapOf(
                    "variable" to functionName,
                    "map" to ValueProvider.vtable(jar.getClassAddress(Type.getObjectType(owner))),
                    "key" to TextValue("$name$desc")
                )
            ))
        } else {
            val method = jar.findMethod(owner, name, desc) ?: throw NullPointerException("Method '$fullName' not found in jar")
            functionName = TextValue(method.functionName)
        }
        context.addOperation(
            JustOperation(
                "call_function", mapOf(
                    "function_name" to functionName,
                    "args" to MapValue(buildMap {
                        if (Type.getReturnType(desc).sort != Type.VOID) {
                            put(Values.valueOf(context.provider.returnVariable.name), variable ?: EmptyValue)
                        }
                        var index = 0
                        if (self != null) {
                            put(Values.valueOf(ValueProvider.localVar(index++).name), self)
                        }
                        for (arg in args) {
                            put(Values.valueOf(ValueProvider.localVar(index++).name), arg.translate(context, null))
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

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "InvokeMethod",
            "method", fullName,
            "self", self,
            "args", args
        )
    }
}