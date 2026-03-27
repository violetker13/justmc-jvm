package me.unidok.jjvm.translator

import me.unidok.jjvm.context.SourceMethod
import me.unidok.jjvm.nativemethod.NativeMethods
import me.unidok.jjvm.operand.*
import me.unidok.jjvm.operation.*
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.replaceOverride
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*
import kotlin.collections.set

class BytecodeTranslator(
    private val sourceMethod: SourceMethod,
    private val bytecode: Iterator<AbstractInsnNode>
) {
    private val inlineMethodsStack = HashSet<SourceMethod>()
    private var line = -1

    private fun getLabelIndex(label: Label): Int {
        return sourceMethod.labelIndexes[label] ?: -1
    }

    private fun getGotoIndex(label: Label): Int {
        return sourceMethod.gotoIndexes[label] ?: -1
    }

    private fun isCycle(label: Label): Boolean {
        val labelIndex = sourceMethod.labelIndexes[label] ?: return false
        val gotoIndex = sourceMethod.gotoIndexes[label] ?: return false
        return labelIndex < gotoIndex
    }

    fun translate(
        destination: MutableList<Operation>,
        end: Label?
    ): Label? {
        val itr = OperationIterator(end)
        while (itr.hasNext()) {
            try {
                destination.add(itr.next())
            } catch (e: Exception) {
                throw Exception("Translate exception ${sourceMethod.sourceClass.node.sourceFile}:$line", e)
            }
        }
        return itr.end
    }

    private inner class OperationIterator(
        var end: Label?
    ) : Iterator<Operation> {
        private var next: Operation? = nextOp()

        override fun hasNext(): Boolean = next != null

        override fun next(): Operation = next?.also { next = nextOp() } ?: throw NoSuchElementException()

        fun nextOp(): Operation? {
            val sourceMethod = sourceMethod
            val inlineVariables = sourceMethod.inlineVariables
            val stack = sourceMethod.stack
            val iterator = bytecode
            while (iterator.hasNext()) {
                val inst = iterator.next()
                when (val opcode = inst.opcode) {
                    -1 -> when (inst.type) {
                        AbstractInsnNode.LABEL -> {
                            val label = (inst as LabelNode).label
                            if (label === end) {
                                end = null
                                return null
                            }
                            if (isCycle(label)) {
                                val loopOperations = ArrayList<Operation>()
                                translate(loopOperations, label)
                                return LoopBranch(loopOperations)
                            }
                        }
                        AbstractInsnNode.LINE -> {
                            line = (inst as LineNumberNode).line
                        }
                    }
                    ACONST_NULL -> stack.addFirst(NullConst)
                    ICONST_M1 -> stack.addFirst(IntConst.CONST_M1)
                    ICONST_0, LCONST_0 -> stack.addFirst(IntConst.CONST_0)
                    ICONST_1, LCONST_1 -> stack.addFirst(IntConst.CONST_1)
                    ICONST_2 -> stack.addFirst(IntConst.CONST_2)
                    ICONST_3 -> stack.addFirst(IntConst.CONST_3)
                    ICONST_4 -> stack.addFirst(IntConst.CONST_4)
                    ICONST_5 -> stack.addFirst(IntConst.CONST_5)
                    FCONST_0, DCONST_0 -> stack.addFirst(DoubleConst.CONST_0)
                    FCONST_1, DCONST_1 -> stack.addFirst(DoubleConst.CONST_1)
                    FCONST_2 -> stack.addFirst(DoubleConst.CONST_2)
                    BIPUSH -> stack.addFirst(IntConst.cache[(inst as IntInsnNode).operand + 128])
                    SIPUSH -> stack.addFirst(IntConst((inst as IntInsnNode).operand))
                    LDC -> when (val value = (inst as LdcInsnNode).cst) {
                        is Int -> stack.addFirst(IntConst(value))
                        is Long -> stack.addFirst(LoadLongConstant(value))
                        is Float -> stack.addFirst(DoubleConst(value.toDouble()))
                        is Double -> stack.addFirst(DoubleConst(value))
                        is String -> stack.addFirst(LoadStringConstant(value))
                        is Type -> stack.addFirst(LoadTypeConstant(value))
                    }
                    ILOAD, LLOAD, FLOAD, DLOAD, ALOAD -> {
                        val local = (inst as VarInsnNode).`var`
                        stack.addFirst(inlineVariables[local] ?: LoadFromLocal(local))
                    }
                    IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD -> {
                        val index = stack.removeFirst()
                        val array = stack.removeFirst()
                        stack.addFirst(OperationResult(LoadFromArray(array, index)))
                    }
                    ISTORE, LSTORE, FSTORE, DSTORE, ASTORE -> {
                        val value = stack.removeFirst()
                        val local = (inst as VarInsnNode).`var`
                        ((value as? OperationResult)?.operation as? MethodOperation)?.let {
                            if (Type.getReturnType(it.desc).internalName == "justmc/Variable") {
                                println("STORE TO INLINE $local = $value")
                                inlineVariables[local] = value
                                return nextOp()
                            }
                        }
                        return StoreToLocal(local, value)

                    }
                    IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE -> {
                        val value = stack.removeFirst()
                        val index = stack.removeFirst()
                        val array = stack.removeFirst()
                        return StoreToArray(array, index, value)
                    }
                    POP, POP2 -> {
                        val value = stack.removeFirst()
                        if (value is OperationResult) return value.operation
                    }
                    DUP, DUP2 -> stack.addFirst(stack.first())
                    DUP_X1, DUP2_X1 -> {
                        val value = stack.removeFirst()
                        val x1 = stack.removeFirst()
                        stack.addFirst(value)
                        stack.addFirst(x1)
                        stack.addFirst(value)
                    }
                    DUP_X2, DUP2_X2 -> {
                        val value = stack.removeFirst()
                        val x1 = stack.removeFirst()
                        val x2 = stack.removeFirst()
                        stack.addFirst(value)
                        stack.addFirst(x2)
                        stack.addFirst(x1)
                        stack.addFirst(value)
                    }
                    SWAP -> {
                        val value1 = stack.removeFirst()
                        val value2 = stack.removeFirst()
                        stack.addFirst(value1)
                        stack.addFirst(value2)
                    }
                    IADD, LADD, FADD, DADD -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(PluralOperator(PluralOperator.Type.ADD, first, second)))
                    }
                    ISUB, LSUB, FSUB, DSUB -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(PluralOperator(PluralOperator.Type.SUB, first, second)))
                    }
                    IMUL, LMUL, FMUL, DMUL -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(PluralOperator(PluralOperator.Type.MUL, first, second)))
                    }
                    IDIV, LDIV -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(PluralOperator(PluralOperator.Type.IDIV, first, second)))
                    }
                    FDIV, DDIV -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(PluralOperator(PluralOperator.Type.FDIV, first, second)))
                    }
                    IREM, LREM, FREM, DREM -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(BinaryOperator(BinaryOperator.Type.REM, first, second)))
                    }
                    INEG, LNEG, FNEG, DNEG -> {
                        stack.addFirst(OperationResult(UnaryOperator(UnaryOperator.Type.NEG, stack.removeFirst())))
                    }
                    ISHL, LSHL -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(BinaryOperator(BinaryOperator.Type.SHL, first, second)))
                    }
                    ISHR, LSHR -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(BinaryOperator(BinaryOperator.Type.SHR, first, second)))
                    }
                    IUSHR, LUSHR -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(BinaryOperator(BinaryOperator.Type.USHR, first, second)))
                    }
                    IAND, LAND -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(BinaryOperator(BinaryOperator.Type.AND, first, second)))
                    }
                    IOR, LOR -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(BinaryOperator(BinaryOperator.Type.OR, first, second)))
                    }
                    IXOR, LXOR -> {
                        val second = stack.removeFirst()
                        val first = stack.removeFirst()
                        stack.addFirst(OperationResult(BinaryOperator(BinaryOperator.Type.XOR, first, second)))
                    }
                    IINC -> {
                        val inst = inst as IincInsnNode
                        return Increment(inst.`var`, inst.incr)
                    }
                    L2I, D2I, F2I, F2L, I2B, I2C, I2S -> {
                        var type: UnaryOperator.Type? = null
                        when (opcode) {
                            L2I, D2I -> type = UnaryOperator.Type.L2I
                            F2I, F2L -> type = UnaryOperator.Type.F2L
                            I2B -> type = UnaryOperator.Type.I2B
                            I2C -> type = UnaryOperator.Type.I2C
                            I2S -> type = UnaryOperator.Type.I2S
                        }
                        stack.addFirst(OperationResult(UnaryOperator(type!!, stack.removeFirst())))
                    }
                    IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT,
                    IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, IFNULL, IFNONNULL -> {
                        var type: IfBranch.Type? = null
                        var operand1: Operand? = null
                        var operand2: Operand? = null
                        when (opcode) {
                            IFEQ, IFNULL -> {
                                type = IfBranch.Type.EQ
                                operand1 = stack.removeFirst()
                                operand2 = IntConst.CONST_0
                            }
                            IFNE, IFNONNULL -> {
                                type = IfBranch.Type.NE
                                operand1 = stack.removeFirst()
                                operand2 = IntConst.CONST_0
                            }
                            IFLT -> {
                                type = IfBranch.Type.LT
                                operand1 = stack.removeFirst()
                                operand2 = IntConst.CONST_0
                            }
                            IFGE -> {
                                type = IfBranch.Type.GE
                                operand1 = stack.removeFirst()
                                operand2 = IntConst.CONST_0
                            }
                            IFGT -> {
                                type = IfBranch.Type.GT
                                operand1 = stack.removeFirst()
                                operand2 = IntConst.CONST_0
                            }
                            IFLE -> {
                                type = IfBranch.Type.LE
                                operand1 = stack.removeFirst()
                                operand2 = IntConst.CONST_0
                            }
                            IF_ICMPEQ, IF_ACMPEQ -> {
                                type = IfBranch.Type.EQ
                                operand2 = stack.removeFirst()
                                operand1 = stack.removeFirst()
                            }
                            IF_ICMPNE, IF_ACMPNE -> {
                                type = IfBranch.Type.NE
                                operand2 = stack.removeFirst()
                                operand1 = stack.removeFirst()
                            }
                            IF_ICMPLT -> {
                                type = IfBranch.Type.LT
                                operand2 = stack.removeFirst()
                                operand1 = stack.removeFirst()
                            }
                            IF_ICMPGE -> {
                                type = IfBranch.Type.GE
                                operand2 = stack.removeFirst()
                                operand1 = stack.removeFirst()
                            }
                            IF_ICMPGT -> {
                                type = IfBranch.Type.GT
                                operand2 = stack.removeFirst()
                                operand1 = stack.removeFirst()
                            }
                            IF_ICMPLE -> {
                                type = IfBranch.Type.LE
                                operand2 = stack.removeFirst()
                                operand1 = stack.removeFirst()
                            }
                        }

                        val endIf = (inst as JumpInsnNode).label.label
                        end?.let { end ->
                            val index = getGotoIndex(end)
                            if (index == -1 || getLabelIndex(endIf) <= index) return@let
                            // Если это цикл, и условие прыгает через цикл, то останавливаем его
                            return IfBranch(
                                type!!,
                                operand1!!,
                                operand2!!,
                                listOf(
                                    ExactOperation(JustOperation("control_stop_repeat"))
                                )
                            )
                        }

                        val ifOperations = ArrayList<Operation>()
                        val endElse = translate(ifOperations, endIf)
                        val elseOperations = endElse?.let {
                            val destination = ArrayList<Operation>()
                            val end = translate(destination, endElse)
                            if (end != null) {
                                this.end = end
                                return null
                            }
                            destination
                        }
                        return IfBranch(
                            type!!.inv(),
                            operand1!!,
                            operand2!!,
                            ifOperations,
                            elseOperations
                        )
                    }
                    GOTO -> {
                        val label = (inst as JumpInsnNode).label.label
                        if (isCycle(label)) {
                            end = label
                            return null
                        }
                        if (end === label) {
                            end = null
                            return null
                        }
                    }
                    IRETURN, LRETURN, FRETURN, DRETURN, ARETURN -> return ReturnValue(stack.removeFirst())
                    RETURN -> return Return
                    GETSTATIC -> {
                        val inst = inst as FieldInsnNode
                        val owner = inst.owner.replaceOverride()
                        val name = inst.name
                        stack.addFirst(NativeMethods.findField(owner, name) ?: GetStatic(owner, name))
                    }
                    PUTSTATIC -> {
                        val inst = inst as FieldInsnNode
                        return PutStatic(inst.owner.replaceOverride(), inst.name, stack.removeFirst())
                    }
                    GETFIELD -> {
                        val inst = inst as FieldInsnNode
                        stack.addFirst(OperationResult(GetField(inst.owner.replaceOverride(), inst.name, inst.desc.replaceOverride(), stack.removeFirst())))
                    }
                    PUTFIELD -> {
                        val value = stack.removeFirst()
                        val owner = stack.removeFirst()
                        val inst = inst as FieldInsnNode
                        return PutField(inst.owner.replaceOverride(), inst.name, inst.desc.replaceOverride(), owner, value)
                    }
                    INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE -> {
                        val inst = inst as MethodInsnNode
                        var owner = inst.owner.replaceOverride()
                        val name = inst.name
                        val desc = inst.desc.replaceOverride()
                        var local = Type.getArgumentCount(desc)
                        if (name == "<init>") ++local
                        @Suppress("UNCHECKED_CAST")
                        val args = arrayOfNulls<Operand>(local) as Array<Operand>
                        while (--local >= 0) {
                            args[local] = stack.removeFirst()
                        }
                        var self: Operand? = null
                        if (opcode == INVOKEVIRTUAL || opcode == INVOKEINTERFACE) {
                            self = stack.removeFirst()
                            //sourceMethod.resolveType(self)?.let { owner = it.internalName } // попытка девиртуализации
                        }
                        val methodName = "$owner.$name$desc"

                        if (methodName == "java/lang/Object.<init>()V") continue

                        NativeMethods.findEarlyMethod(methodName)?.let { method ->
                            val result = method.invoke(self, args, sourceMethod)
                            println("CALL EARLY $methodName $self ${args.joinToString(", ", "[", "]")} at ${sourceMethod.fullName} = $result")
                            if (result != null) {
                                sourceMethod.resolvedTypes[result] = Type.getReturnType(desc)
                                stack.addFirst(result)
                                continue
                            }
                        }

                        val operation: OperationWithResult = run {
                            val sourceClass = sourceMethod.sourceClass
                            val jar = sourceClass.jar
                            jar.findClass(owner)?.let { clazz ->
                                val method = clazz.methods[methodName] ?: return@let
                                val isInit = name == "<init>"
                                val forceInline = !isInit && method.isAnnotated(Annotations.INLINE)
                                if (!inlineMethodsStack.add(method)) {
                                    if (forceInline) throw IllegalStateException("Inline method '$methodName' cannot be recursive")
                                    if (!isInit) {
                                        inlineMethodsStack.remove(method)
                                        return@let // не пытаемся инлайнить рекурсивные методы
                                    }
                                }
                                if (
                                    isInit ||
                                    forceInline ||
                                    clazz.isAnnotated(Annotations.INLINE) ||
                                    method.getLength() <= jar.config.maxInlineLength ||
                                    NativeMethods.findMethod(methodName) != null
                                ) {
                                    inlineMethodsStack.remove(method)
                                    return@run InlineMethod(method, self, args)
                                }
                                method.calls++
                            }

                            InvokeMethod(
                                owner, name, desc,
                                self != null && !sourceClass.jar.isFinalClass(owner),
                                self, args
                            )
                        }

                        val returnType = Type.getReturnType(desc)
                        if (returnType.sort != Type.VOID) {
                            val result = OperationResult(operation)
                            sourceMethod.resolvedTypes[result] = returnType
                            stack.addFirst(result)
                        } else {
                            return operation
                        }
                    }
                    INVOKEDYNAMIC -> {
                        val inst = inst as InvokeDynamicInsnNode
                        inst.name
                        inst.desc
                        inst.bsm
                        inst.bsmArgs
                    }
                    NEW -> stack.addFirst(OperationResult(New((inst as TypeInsnNode).desc.replaceOverride())))
                    NEWARRAY, ANEWARRAY -> {
                        val size = stack.removeFirst()
                        val operation = NewArray(size)
                        val result = OperationResult(operation)
                        stack.addFirst(result)
                        if (size is IntConst) {
                            println("CONST ARRAY $operation")
                            val values = Array<Operand>(size.value) { IntConst.CONST_0 }
                            sourceMethod.constArrays[operation] = values.asList()
                            val iterator = OperationIterator(end)
                            while (iterator.hasNext()) {
                                val op = iterator.next()
                                if (op !is StoreToArray || op.array != result) return op
                                val index = op.index as? IntConst ?: return op
                                println("CONST ARRAY $operation [$index] = ${op.value}")
                                values[index.value] = op.value
                            }
                        }
                    }
                    ARRAYLENGTH -> stack.addFirst(OperationResult(ArrayLength(stack.removeFirst())))
                    ATHROW -> {}
                }
            }
            end = null
            return null
        }
    }
}