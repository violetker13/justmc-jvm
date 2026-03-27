package me.unidok.jjvm.operation

import me.unidok.jjvm.context.SourceMethod
import me.unidok.jjvm.context.TranslationContext
import me.unidok.jjvm.nativemethod.NativeMethods
import me.unidok.jjvm.translator.ValueProvider
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.OperationResult
import me.unidok.jjvm.util.Debugger
import me.unidok.jjvm.util.appendObject
import me.unidok.jjvm.util.simplify
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

class InlineMethod(
    val method: SourceMethod,
    val self: Operand?,
    val args: Array<out Operand>
) : MethodOperation() {
    private val inlineArgs: Map<Int, Operand>
    private val setArgs: List<StoreToLocal>

    init {
        val inlineArgs = HashMap<Int, Operand>()
        val setArgs = ArrayList<StoreToLocal>()
        var index = 0
        val self = self
        if (self != null) {
            if (self is OperationResult && self.operation is InlineMethod) {
                inlineArgs[index] = self
            } else {
                setArgs.add(StoreToLocal(index, self))
            }
            index++
        }
        for (arg in args) {
            if (arg is OperationResult && arg.operation is InlineMethod) {
                inlineArgs[index] = arg
            } else {
                setArgs.add(StoreToLocal(index, arg))
            }
            index++
        }
        this.inlineArgs = inlineArgs
        this.setArgs = setArgs
    }

    override val owner: String
        get() = method.sourceClass.name

    override val name: String
        get() = method.name

    override val desc: String
        get() = method.desc

    override val fullName: String
        get() = method.fullName

    override val length: Int
        get() = setArgs.sumOf { it.length } + method.getLength()

    private fun inline(context: TranslationContext, variable: Variable?) {
        method.inlineVariables.putAll(inlineArgs)
        val provider = context.provider
        context.copy(
            iterator = iterator {
                yieldAll(setArgs)
                yieldAll(method.operations)
            },
            provider = provider.copy(
                returnVariable = variable ?: ValueProvider.defaultReturnVariable,
                varOffset = provider.varOffset + method.node.maxLocals
            ).also {
                it.resultVariable = variable
            }
        ).translate()
    }

    override fun translate(context: TranslationContext, variable: Variable?): Value {
        NativeMethods.findMethod(fullName)?.let { method ->
            val result = method.invoke(this, context, variable)
            if (result != null) return result
        }
        val variable = variable ?: context.tempVar()
        inline(context, variable)
        return variable
    }

    override fun translate(context: TranslationContext) {
        NativeMethods.findMethod(fullName)?.let { method ->
            method.invoke(this, context, null)
            return
        }
        inline(context, null)
    }

    override fun appendTo(builder: StringBuilder, indent: String) {
        builder.appendObject(
            indent,
            "InlineMethod",
            "method", method.fullName,
            "self", self,
            "args", args
        )
    }
}