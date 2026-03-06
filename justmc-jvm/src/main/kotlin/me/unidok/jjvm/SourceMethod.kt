package me.unidok.jjvm

import me.unidok.jjvm.nativemethod.NativeMethods
import me.unidok.jjvm.operation.InvokeMethod
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operation.OperationWithResult
import me.unidok.jjvm.operation.Operation
import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.jjvm.util.isAnnotated
import me.unidok.justcode.value.Value
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*

class SourceMethod(
    val sourceClass: SourceClass,
    val node: MethodNode
) {
    val fullName = "${sourceClass.node.name}.${node.name}${node.desc}"
    val labelIndexes = HashMap<Label, Int>()
    val gotoIndexes = HashMap<Label, Int>()
    val resolvedTypes = HashMap<Operand, Type>()
    val translated = HashMap<OperationWithResult, Value>()
    val stack = ArrayDeque<Operand>(node.maxStack)
    val operations = ArrayList<Operation>()
    val inlineMethodsStack = HashSet<SourceMethod>()
    var calls = 0

    fun isFinalClassInstance(operand: Operand): Boolean {
        val type = resolveType(operand)
        return type != null && sourceClass.jar.isFinalClass(type.internalName)
    }

    fun resolveType(operand: Operand): Type? {
        val type = resolvedTypes[operand]
        if (type != null) return type
        return null
    }
}