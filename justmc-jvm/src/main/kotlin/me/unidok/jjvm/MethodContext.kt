package me.unidok.jjvm

import me.unidok.jjvm.operand.InvokeMethod
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.SummaryOperand
import me.unidok.jjvm.operation.Operation
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.jjvm.util.Translator.localName
import me.unidok.jjvm.util.isAnnotated
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*

class MethodContext(
    val source: SourceContext,
    val clazz: ClassNode,
    val method: MethodNode
) {
    val labelsIndexes = HashMap<Label, Int>()
    val gotosIndexes = HashMap<Label, Int>()
    val resolvedTypes = HashMap<Operand, Type>()
    val translatedOperands = HashMap<SummaryOperand, Value>()
    val stack = ArrayDeque<Operand>(method.maxStack)
    val operations = ArrayList<Operation>()
    val translatedOperations = ArrayList<JustOperation>()
    private val inlineMethodsStack = HashSet<MethodNode>()
    var varOffset = 0
    private var temps = 0

    fun tempVar(): Variable {
        return Variable("t${temps++}", Variable.Scope.LINE)
    }

    fun localVar(n: Int): Variable {
        return Variable(localName(n + varOffset), Variable.Scope.LINE)
    }

    fun isFinalClassInstance(operand: Operand): Boolean {
        val type = resolveType(operand)
        return type != null && source.isFinalClass(type.internalName)
    }

    fun resolveType(operand: Operand): Type? {
        val type = resolvedTypes[operand]
        if (type != null) return type
        return null
    }

    fun translateBytecode() {
        method.instructions.forEachIndexed { index, inst ->
            when (inst.opcode) {
                -1 -> if (inst.type == AbstractInsnNode.LABEL) {
                    labelsIndexes[(inst as LabelNode).label] = index
                }
                Opcodes.GOTO -> {
                    gotosIndexes[(inst as JumpInsnNode).label.label] = index
                }
            }
        }
        BytecodeTranslator(this, method.instructions.iterator()).translate(operations)
    }

    fun translateOperations(context: TranslationContext) {
        translateOperations(operations, context)
    }

    fun translateOperations(
        operations: List<Operation>,
        context: TranslationContext
    ) {
        val source = source
        for (operation in operations) {
            when (operation) {
                is InvokeMethod -> {
                    source.findClass(operation.owner)?.let { clazz ->
                        val method = source.findClassMethod(clazz, operation.name, operation.desc) ?: return@let
                        val isInline = method.isAnnotated(Annotations.INLINE) || clazz.isAnnotated(Annotations.INLINE)
                        if (!inlineMethodsStack.add(method)) {
                            if (isInline) {
                                val methodName = Translator.methodName(clazz.name, method.name, method.desc)
                                throw TranslateException("Inline method '$methodName' cannot be recursive")
                            }
                            inlineMethodsStack.remove(method)
                            return@let // рекурсивные методы не инлайним
                        }
                        val methodContext = source.contexts[method]!!
                        methodContext.varOffset += method.maxLocals
                        if (isInline) {
                            methodContext.translateOperations(context)
                        } else {
                            val operations = ArrayList<JustOperation>()
                            methodContext.translateOperations(TranslationContext(methodContext, operations))
                            if (operations.size <= source.config.inlineActionsUntil) {
                                context.operations.addAll(operations)
                            }
                        }
                        methodContext.varOffset -= method.maxLocals
                    }
                }
                else -> operation.translate(context)
            }
        }
    }
}