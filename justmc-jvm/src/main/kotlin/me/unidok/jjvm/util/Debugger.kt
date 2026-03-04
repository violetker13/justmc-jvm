package me.unidok.jjvm.util

import me.unidok.jjvm.MethodContext
import me.unidok.jjvm.operation.IfBranch
import me.unidok.jjvm.operation.LoopBranch
import me.unidok.jjvm.operation.Operation
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.*

object Debugger {
    fun toString(inst: AbstractInsnNode): String = buildString {
        val opcode = inst.opcode
        append(opcode)
        append('\t')
        opcodeNames.getOrNull(opcode)?.let { name ->
            append(name)
            append('\t')
        }
        when (inst) {
            is IntInsnNode -> append(inst.operand)
            is VarInsnNode -> {
                append('#')
                append(inst.`var`)
            }
            is TypeInsnNode -> append(inst.desc)
            is FieldInsnNode -> {
                append(inst.owner)
                append('\t')
                append(inst.name)
                append('\t')
                append(inst.desc)
            }
            is MethodInsnNode -> {
                append(inst.owner)
                append('\t')
                append(inst.name)
                append('\t')
                append(inst.desc)
                append('\t')
                append(inst.itf)
            }
            // TODO
            is InvokeDynamicInsnNode -> append("invoke dynamic: desc: '${inst.desc}'; name: '${inst.name}'; bsm: '${inst.bsm}'; bsm name: '${inst.bsm.name}'; bsm desc: '${inst.bsm.desc}'; bsm owher: '${inst.bsm.owner}'; bsm tag: '${inst.bsm.tag}'; bsm interface: '${inst.bsm.isInterface}'; bsmArgs: '${inst.bsmArgs}'")
            is JumpInsnNode -> append("jump: ${inst.label.label}")
            is LabelNode -> append("lable: ${inst.label}")
            is LdcInsnNode -> append("${inst.cst}")
            is IincInsnNode -> append("#${inst.`var`} += ${inst.incr}")
            is TableSwitchInsnNode -> append("table switch: min: ${inst.min}; max: ${inst.max}; label info: '${inst.dflt.label}' labels: '${inst.labels.joinToString {it.label.toString()}}'")
            is LookupSwitchInsnNode -> append("lookup switch: keys: '${inst.keys.joinToString()}', dflt: '${inst.dflt.label}'; labels: '${inst.labels.joinToString {it.label.toString()}}'")
            is MultiANewArrayInsnNode -> append("multi a new array: dims: ${inst.dims}; desc: '${inst.desc}'")
            is FrameNode -> append("frame: type: ${inst.type}; local: '${inst.local.joinToString()}'; stack: '${inst.stack.joinToString()}'")
            is LineNumberNode -> append("line number: ${inst.line} start: '${inst.start.label}'")
            else -> {}
        }
    }

    fun debugClass(clazz: ClassNode) {
        println("Source file: ${clazz.sourceFile}")
        println("Source debug: ${clazz.sourceDebug}")
        println("Visible annotations: ${clazz.visibleAnnotations?.joinToString { "${it.desc} ${it.values?.joinToString()} "}}")
        println("Invisible annotations: ${clazz.invisibleAnnotations?.joinToString { "$${it.desc} ${it.values?.joinToString()} "}}")
        println("Analyzing class: ${clazz.name} extends ${clazz.superName} implements ${clazz.interfaces.joinToString()}")
    }

    fun debugMethod(method: MethodNode) {
        println("\tVisible annotations: ${method.visibleAnnotations?.joinToString { "${it.desc} ${it.values?.joinToString()} "}}")
        println("\tInvisible annotations: ${method.invisibleAnnotations?.joinToString { "$${it.desc} ${it.values?.joinToString()} "}}")
        println("\tMethod: ${method.name} ${method.desc} maxLocals=${method.maxLocals} maxStack=${method.maxStack}")
        println("\tBytecode:")
        for (inst in method.instructions) {
            println("\t\t${toString(inst)}")
        }
    }

    private fun debugOperations(prefix: String, operations: List<Operation>) {
        operations.forEach {
            when (it) {
                is LoopBranch -> {
                    println("${prefix}LOOP")
                    debugOperations("\t$prefix", it.operations)
                }
                is IfBranch -> {
                    println("${prefix}IF ${it.operand1} ${it.type} ${it.operand2}")
                    debugOperations("\t$prefix", it.operations)
                    it.otherwise?.let { otherwise ->
                        println("${prefix}ELSE")
                        debugOperations("\t$prefix", otherwise)
                    }
                }
                else -> println("$prefix$it")
            }
        }
    }

    fun debugIR(source: MethodContext) {
        println("\tIR:")
        debugOperations("\t\t", source.operations)
        println()
    }

    private val opcodeNames = arrayOfNulls<String>(200).also { arr ->
        arr[NOP] = "NOP"
        arr[ACONST_NULL] = "ACONST_NULL"
        arr[ICONST_M1] = "ICONST_M1"
        arr[ICONST_0] = "ICONST_0"
        arr[ICONST_1] = "ICONST_1"
        arr[ICONST_2] = "ICONST_2"
        arr[ICONST_3] = "ICONST_3"
        arr[ICONST_4] = "ICONST_4"
        arr[ICONST_5] = "ICONST_5"
        arr[LCONST_0] = "LCONST_0"
        arr[LCONST_1] = "LCONST_1"
        arr[FCONST_0] = "FCONST_0"
        arr[FCONST_1] = "FCONST_1"
        arr[FCONST_2] = "FCONST_2"
        arr[DCONST_0] = "DCONST_0"
        arr[DCONST_1] = "DCONST_1"
        arr[BIPUSH] = "BIPUSH"
        arr[SIPUSH] = "SIPUSH"
        arr[LDC] = "LDC"
        arr[ILOAD] = "ILOAD"
        arr[LLOAD] = "LLOAD"
        arr[FLOAD] = "FLOAD"
        arr[DLOAD] = "DLOAD"
        arr[ALOAD] = "ALOAD"
        arr[IALOAD] = "IALOAD"
        arr[LALOAD] = "LALOAD"
        arr[FALOAD] = "FALOAD"
        arr[DALOAD] = "DALOAD"
        arr[AALOAD] = "AALOAD"
        arr[BALOAD] = "BALOAD"
        arr[CALOAD] = "CALOAD"
        arr[SALOAD] = "SALOAD"
        arr[ISTORE] = "ISTORE"
        arr[LSTORE] = "LSTORE"
        arr[FSTORE] = "FSTORE"
        arr[DSTORE] = "DSTORE"
        arr[ASTORE] = "ASTORE"
        arr[IASTORE] = "IASTORE"
        arr[LASTORE] = "LASTORE"
        arr[FASTORE] = "FASTORE"
        arr[DASTORE] = "DASTORE"
        arr[AASTORE] = "AASTORE"
        arr[BASTORE] = "BASTORE"
        arr[CASTORE] = "CASTORE"
        arr[SASTORE] = "SASTORE"
        arr[POP] = "POP"
        arr[POP2] = "POP2"
        arr[DUP] = "DUP"
        arr[DUP_X1] = "DUP_X1"
        arr[DUP_X2] = "DUP_X2"
        arr[DUP2] = "DUP2"
        arr[DUP2_X1] = "DUP2_X1"
        arr[DUP2_X2] = "DUP2_X2"
        arr[SWAP] = "SWAP"
        arr[IADD] = "IADD"
        arr[LADD] = "LADD"
        arr[FADD] = "FADD"
        arr[DADD] = "DADD"
        arr[ISUB] = "ISUB"
        arr[LSUB] = "LSUB"
        arr[FSUB] = "FSUB"
        arr[DSUB] = "DSUB"
        arr[IMUL] = "IMUL"
        arr[LMUL] = "LMUL"
        arr[FMUL] = "FMUL"
        arr[DMUL] = "DMUL"
        arr[IDIV] = "IDIV"
        arr[LDIV] = "LDIV"
        arr[FDIV] = "FDIV"
        arr[DDIV] = "DDIV"
        arr[IREM] = "IREM"
        arr[LREM] = "LREM"
        arr[FREM] = "FREM"
        arr[DREM] = "DREM"
        arr[INEG] = "INEG"
        arr[LNEG] = "LNEG"
        arr[FNEG] = "FNEG"
        arr[DNEG] = "DNEG"
        arr[ISHL] = "ISHL"
        arr[LSHL] = "LSHL"
        arr[ISHR] = "ISHR"
        arr[LSHR] = "LSHR"
        arr[IUSHR] = "IUSHR"
        arr[LUSHR] = "LUSHR"
        arr[IAND] = "IAND"
        arr[LAND] = "LAND"
        arr[IOR] = "IOR"
        arr[LOR] = "LOR"
        arr[IXOR] = "IXOR"
        arr[LXOR] = "LXOR"
        arr[IINC] = "IINC"
        arr[I2L] = "I2L"
        arr[I2F] = "I2F"
        arr[I2D] = "I2D"
        arr[L2I] = "L2I"
        arr[L2F] = "L2F"
        arr[L2D] = "L2D"
        arr[F2I] = "F2I"
        arr[F2L] = "F2L"
        arr[F2D] = "F2D"
        arr[D2I] = "D2I"
        arr[D2L] = "D2L"
        arr[D2F] = "D2F"
        arr[I2B] = "I2B"
        arr[I2C] = "I2C"
        arr[I2S] = "I2S"
        arr[LCMP] = "LCMP"
        arr[FCMPL] = "FCMPL"
        arr[FCMPG] = "FCMPG"
        arr[DCMPL] = "DCMPL"
        arr[DCMPG] = "DCMPG"
        arr[IFEQ] = "IFEQ"
        arr[IFNE] = "IFNE"
        arr[IFLT] = "IFLT"
        arr[IFGE] = "IFGE"
        arr[IFGT] = "IFGT"
        arr[IFLE] = "IFLE"
        arr[IF_ICMPEQ] = "IF_ICMPEQ"
        arr[IF_ICMPNE] = "IF_ICMPNE"
        arr[IF_ICMPLT] = "IF_ICMPLT"
        arr[IF_ICMPGE] = "IF_ICMPGE"
        arr[IF_ICMPGT] = "IF_ICMPGT"
        arr[IF_ICMPLE] = "IF_ICMPLE"
        arr[IF_ACMPEQ] = "IF_ACMPEQ"
        arr[IF_ACMPNE] = "IF_ACMPNE"
        arr[GOTO] = "GOTO"
        arr[JSR] = "JSR"
        arr[RET] = "RET"
        arr[TABLESWITCH] = "TABLESWITCH"
        arr[LOOKUPSWITCH] = "LOOKUPSWITCH"
        arr[IRETURN] = "IRETURN"
        arr[LRETURN] = "LRETURN"
        arr[FRETURN] = "FRETURN"
        arr[DRETURN] = "DRETURN"
        arr[ARETURN] = "ARETURN"
        arr[RETURN] = "RETURN"
        arr[GETSTATIC] = "GETSTATIC"
        arr[PUTSTATIC] = "PUTSTATIC"
        arr[GETFIELD] = "GETFIELD"
        arr[PUTFIELD] = "PUTFIELD"
        arr[INVOKEVIRTUAL] = "INVOKEVIRTUAL"
        arr[INVOKESPECIAL] = "INVOKESPECIAL"
        arr[INVOKESTATIC] = "INVOKESTATIC"
        arr[INVOKEINTERFACE] = "INVOKEINTERFACE"
        arr[INVOKEDYNAMIC] = "INVOKEDYNAMIC"
        arr[NEW] = "NEW"
        arr[NEWARRAY] = "NEWARRAY"
        arr[ANEWARRAY] = "ANEWARRAY"
        arr[ARRAYLENGTH] = "ARRAYLENGTH"
        arr[ATHROW] = "ATHROW"
        arr[CHECKCAST] = "CHECKCAST"
        arr[INSTANCEOF] = "INSTANCEOF"
        arr[MONITORENTER] = "MONITORENTER"
        arr[MONITOREXIT] = "MONITOREXIT"
        arr[MULTIANEWARRAY] = "MULTIANEWARRAY"
        arr[IFNULL] = "IFNULL"
        arr[IFNONNULL] = "IFNONNULL"
    }
}