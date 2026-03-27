package me.unidok.jjvm.context

import me.unidok.jjvm.translator.BytecodeTranslator
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operation.New
import me.unidok.jjvm.operation.NewArray
import me.unidok.jjvm.operation.Operation
import me.unidok.jjvm.operation.PutField
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.getAnnotation
import me.unidok.jjvm.util.isAnnotated
import me.unidok.jjvm.util.replaceOverride
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodNode
import kotlin.collections.set

class SourceMethod(
    val sourceClass: SourceClass,
    val node: MethodNode
) {
    val stack = ArrayDeque<Operand>(node.maxStack)
    val operations = ArrayList<Operation>()
    val labelIndexes = HashMap<Label, Int>()
    val gotoIndexes = HashMap<Label, Int>()
    val resolvedTypes = HashMap<Operand, Type>()
    val inlineVariables = HashMap<Int, Operand>()
    val constArrays = HashMap<NewArray, List<Operand>>()
    var calls = 0

    val name: String get() = node.name
    val desc: String = node.desc.replaceOverride()
    val access: Int get() = node.access
    val fullName = "${sourceClass.name}.${name}${desc}"
    val functionName = getAnnotation(Annotations.FUNCTION_NAME)?.get("name") as? String ?: fullName

    fun isAnnotated(annotation: String): Boolean =
        node.invisibleAnnotations.isAnnotated(annotation)

    fun getAnnotation(annotation: String): Map<String, Any>? =
        node.invisibleAnnotations.getAnnotation(annotation)

    fun isFinalClassInstance(operand: Operand): Boolean {
        val type = resolveType(operand)
        return type != null && sourceClass.jar.isFinalClass(type.internalName)
    }

    fun resolveType(operand: Operand): Type? {
        val type = resolvedTypes[operand]
        if (type != null) return type
        return null
    }

    private var length = -1

    fun getLength(): Int {
        if (length == -1) {
            length = operations.sumOf { it.length }
        }
        return length
    }

    fun translateBytecode() {
        node.instructions.forEachIndexed { index, inst ->
            when (inst.opcode) {
                -1 -> if (inst.type == AbstractInsnNode.LABEL) {
                    labelIndexes[(inst as LabelNode).label] = index
                }
                Opcodes.GOTO -> {
                    gotoIndexes[(inst as JumpInsnNode).label.label] = index
                }
            }
        }
        BytecodeTranslator(this, node.instructions.iterator()).translate(operations, null)
    }

    override fun toString(): String = fullName
}