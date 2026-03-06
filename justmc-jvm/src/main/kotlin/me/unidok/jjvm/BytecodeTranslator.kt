package me.unidok.jjvm

import me.unidok.jjvm.nativemethod.NativeMethods
import me.unidok.jjvm.operand.*
import me.unidok.jjvm.operation.*
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.jjvm.util.Values
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*

class BytecodeTranslator(
    val sourceMethod: SourceMethod,
    val bytecode: Iterator<AbstractInsnNode>
) {
    fun getLabelIndex(label: Label): Int {
        return sourceMethod.labelIndexes[label] ?: -1
    }

    fun getGotoIndex(label: Label): Int {
        return sourceMethod.gotoIndexes[label] ?: -1
    }

    fun isCycle(label: Label): Boolean {
        val labelIndex = sourceMethod.labelIndexes[label] ?: return false
        val gotoIndex = sourceMethod.gotoIndexes[label] ?: return false
        return labelIndex < gotoIndex
    }

    fun translate(
        destination: MutableList<Operation>,
        end: Label? = null
    ): Label? {
        val sourceMethod = sourceMethod
        val sourceClass = sourceMethod.sourceClass
        val jar = sourceClass.jar
        val stack = sourceMethod.stack
        val iterator = bytecode
        while (iterator.hasNext()) {
            val inst = iterator.next()
            when (val opcode = inst.opcode) {
                -1 -> when (inst.type) {
                    AbstractInsnNode.LABEL -> {
                        val label = (inst as LabelNode).label
                        if (label === end) return null
                        if (isCycle(label)) {
                            val loopOperations = ArrayList<Operation>()
                            translate(loopOperations, label)
                            destination.add(LoopBranch(loopOperations))
                        }
                    }
                    AbstractInsnNode.LINE -> if (jar.config.sourceLineNumbers) {
                        val lineNumber = (inst as LineNumberNode).line
                        if (lineNumber != -1) {
                            destination.add(NonAffectOperation(
                                Translator.setVariable(
                                    Translator.LINE_NUMBER_VARIABLE,
                                    Values.valueOf(lineNumber)
                                )
                            ))
                        }
                    }
                }
                ACONST_NULL -> stack.addFirst(AddressConst.CONST_NULL)
                ICONST_M1 -> stack.addFirst(IntegerConst.CONST_M1)
                ICONST_0, LCONST_0 -> stack.addFirst(IntegerConst.CONST_0)
                ICONST_1, LCONST_1 -> stack.addFirst(IntegerConst.CONST_1)
                ICONST_2 -> stack.addFirst(IntegerConst.CONST_2)
                ICONST_3 -> stack.addFirst(IntegerConst.CONST_3)
                ICONST_4 -> stack.addFirst(IntegerConst.CONST_4)
                ICONST_5 -> stack.addFirst(IntegerConst.CONST_5)
                FCONST_0, DCONST_0 -> stack.addFirst(FloatConst.CONST_0)
                FCONST_1, DCONST_1 -> stack.addFirst(FloatConst.CONST_1)
                FCONST_2 -> stack.addFirst(FloatConst.CONST_2)
                BIPUSH -> stack.addFirst(IntegerConst.cache[(inst as IntInsnNode).operand])
                SIPUSH -> stack.addFirst(IntegerConst((inst as IntInsnNode).operand))
                LDC -> {
                    val value = (inst as LdcInsnNode).cst
                    when (value) {
                        is Int -> stack.addFirst(IntegerConst(value))
                        is Long -> stack.addFirst(IntegerConst(value))
                        is Float -> stack.addFirst(FloatConst(value.toDouble()))
                        is Double -> stack.addFirst(FloatConst(value))
                        is String -> {
                            stack.addFirst(LoadDynamicConstant())
                            val address = jar.constants.indexOf("java/lang/String")
                        }
                        is Type -> TODO()
                    }
                }
                ILOAD, LLOAD, FLOAD, DLOAD, ALOAD -> stack.addFirst(LoadFromLocal((inst as VarInsnNode).`var`))
                IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD -> {
                    val index = stack.removeFirst()
                    val array = stack.removeFirst()
                    val operation = LoadFromArray(array, index)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                ISTORE, LSTORE, FSTORE, DSTORE, ASTORE -> {
                    val value = stack.removeFirst()
                    val local = (inst as VarInsnNode).`var`
                    destination.add(StoreToLocal(local, value))
                }
                IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE -> {
                    val value = stack.removeFirst()
                    val index = stack.removeFirst()
                    val array = stack.removeFirst()
                    destination.add(StoreToArray(array, index, value))
                }
                POP, POP2 -> stack.removeFirst()
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
                    val operation = PluralOperator(PluralOperator.Type.ADD, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                ISUB, LSUB, FSUB, DSUB -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = PluralOperator(PluralOperator.Type.SUB, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                IMUL, LMUL, FMUL, DMUL -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = PluralOperator(PluralOperator.Type.MUL, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                IDIV, LDIV -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = PluralOperator(PluralOperator.Type.IDIV, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                FDIV, DDIV -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = PluralOperator(PluralOperator.Type.FDIV, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                IREM, LREM, FREM, DREM -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = BinaryOperator(BinaryOperator.Type.REM, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                INEG, LNEG, FNEG, DNEG -> {
                    val operation = UnaryOperator(UnaryOperator.Type.NEG, stack.removeFirst())
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                ISHL, LSHL -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = BinaryOperator(BinaryOperator.Type.SHL, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                ISHR, LSHR -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = BinaryOperator(BinaryOperator.Type.SHR, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                IUSHR, LUSHR -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = BinaryOperator(BinaryOperator.Type.USHR, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                IAND, LAND -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = BinaryOperator(BinaryOperator.Type.AND, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                IOR, LOR -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = BinaryOperator(BinaryOperator.Type.OR, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                IXOR, LXOR -> {
                    val second = stack.removeFirst()
                    val first = stack.removeFirst()
                    val operation = BinaryOperator(BinaryOperator.Type.XOR, first, second)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                IINC -> {
                    val inst = inst as IincInsnNode
                    destination.add(Increment(inst.`var`, inst.incr))
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
                    val operation = UnaryOperator(type!!, stack.removeFirst())
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
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
                            operand2 = IntegerConst.CONST_0
                        }
                        IFNE, IFNONNULL -> {
                            type = IfBranch.Type.NE
                            operand1 = stack.removeFirst()
                            operand2 = IntegerConst.CONST_0
                        }
                        IFLT -> {
                            type = IfBranch.Type.LT
                            operand1 = stack.removeFirst()
                            operand2 = IntegerConst.CONST_0
                        }
                        IFGE -> {
                            type = IfBranch.Type.GE
                            operand1 = stack.removeFirst()
                            operand2 = IntegerConst.CONST_0
                        }
                        IFGT -> {
                            type = IfBranch.Type.GT
                            operand1 = stack.removeFirst()
                            operand2 = IntegerConst.CONST_0
                        }
                        IFLE -> {
                            type = IfBranch.Type.LE
                            operand1 = stack.removeFirst()
                            operand2 = IntegerConst.CONST_0
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
                    if (end != null) {
                        val index = getGotoIndex(end)
                        // Если это цикл, и условие прыгает дальше цикла, то останавливаем его
                        if (index != -1 && getLabelIndex(endIf) > index) {
                            destination.add(IfBranch(
                                type!!,
                                operand1!!,
                                operand2!!,
                                listOf(
                                    NonAffectOperation(JustOperation("control_stop_repeat"))
                                )
                            ))
                            continue
                        }
                    }

                    val ifOperations = ArrayList<Operation>()
                    val endElse = translate(ifOperations, endIf)
                    val elseOperations = if (endElse == null) null else buildList<Operation> {
                        val end = translate(this, endElse)
                        if (end != null) return end
                    }
                    destination.add(IfBranch(
                        type!!.inv(),
                        operand1!!,
                        operand2!!,
                        ifOperations,
                        elseOperations
                    ))
                }
                GOTO -> {
                    val label = (inst as JumpInsnNode).label.label
                    if (isCycle(label)) return label
                    if (end === label) return null
                }
                RETURN, IRETURN, LRETURN, FRETURN, DRETURN, ARETURN -> {
                    if (opcode != RETURN) destination.add(TranslatableFutureOperation {
                        Translator.setVariable(this, Translator.RETURN_VARIABLE, stack.removeFirst())
                    })
                    if (inst !== sourceMethod.node.instructions.last) {
                        destination.add(NonAffectOperation(Translator.returnMethod(sourceMethod)))
                    }
                }
                GETSTATIC -> {
                    val inst = inst as FieldInsnNode
                    val owner = inst.owner
                    val name = inst.name
                    stack.addFirst(NativeMethods.findField(owner, name) ?: GetStatic(owner, name))
                }
                PUTSTATIC -> {
                    val inst = inst as FieldInsnNode
                    destination.add(PutStatic(inst.owner, inst.name, stack.removeFirst()))
                }
                GETFIELD -> {
                    val operation = GetField(stack.removeFirst(), (inst as FieldInsnNode).name)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                PUTFIELD -> {
                    val value = stack.removeFirst()
                    val owner = stack.removeFirst()
                    destination.add(PutField(owner, (inst as FieldInsnNode).name, value))
                }
                INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE -> {
                    val inst = inst as MethodInsnNode
                    val name = inst.name
                    val desc = inst.desc
                    var local = Type.getArgumentCount(desc)
                    if (name == "<init>") ++local
                    val args = arrayOfNulls<Operand>(local)
                    while (--local >= 0) {
                        args[local] = stack.removeFirst()
                    }
                    var self: Operand? = null
                    val owner: String = if (opcode == INVOKEVIRTUAL || opcode == INVOKEINTERFACE) {
                        self = stack.removeFirst()
                        sourceMethod.resolveType(self)?.internalName ?: inst.owner // попытка девиртуализации
                    } else {
                        inst.owner
                    }
                    @Suppress("UNCHECKED_CAST")
                    val method = InvokeMethod(
                        owner, name, desc,
                        sourceClass.jar.isFinalClass(owner),
                        self, args as Array<out Operand>
                    )
                    sourceMethod.resolvedTypes[OperationResult(method)] = Type.getReturnType(desc)
                    destination.add(method)
                }
                INVOKEDYNAMIC -> {
                    inst as InvokeDynamicInsnNode
                    inst.name
                    inst.desc
                    inst.bsm
                    inst.bsmArgs
                }
                NEW -> {
                    val operation = New((inst as TypeInsnNode).desc)
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                NEWARRAY, ANEWARRAY -> {
                    val operation = NewArray(stack.removeFirst())
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                ARRAYLENGTH -> {
                    val operation = ArrayLength(stack.removeFirst())
                    destination.add(operation)
                    stack.addFirst(OperationResult(operation))
                }
                ATHROW -> {
                }
            }
        }
        return null
    }
}