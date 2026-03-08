package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslateException
import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.ValueProvider
import me.unidok.jjvm.nativemethod.NativeMethods
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Values
import me.unidok.jjvm.util.isAnnotated
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
    @JvmField val virtual: Boolean,
    @JvmField val self: Operand?,
    @JvmField val args: Array<out Operand>
) : OperationWithResult {
    private fun translate0(context: TranslationContext, variable: Variable?) {
        val owner = owner
        val name = name
        val desc = desc
        val methodName = "$owner.$name$desc"
        val sourceMethod = context.sourceMethod
        val jar = sourceMethod.sourceClass.jar
        val args = args
        val self = self?.translate(context, null)
        val functionName = if (virtual) {
            TextValue("%entry(${ValueProvider.vtable(jar.getClassAddress(Type.getObjectType(owner))).name},$name$desc)")
        } else {
            TextValue(methodName)
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

    override fun toString(): String = "InvokeMethod(owner=$owner, name=$name, desc=$desc, self=$self, args=${args.joinToString(", ", "[", "]")})"
}