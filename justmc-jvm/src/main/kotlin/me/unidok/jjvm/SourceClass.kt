package me.unidok.jjvm

import me.unidok.jjvm.util.Annotations
import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.Translator
import me.unidok.jjvm.util.getAnnotation
import me.unidok.justcode.Handlers
import me.unidok.justcode.trigger.EventTrigger
import me.unidok.justcode.trigger.FunctionTrigger
import me.unidok.justcode.trigger.Trigger
import me.unidok.justcode.value.ArrayValue
import me.unidok.justcode.value.LocalizedText
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.ValueType
import me.unidok.justcode.value.parameter.Parameter
import me.unidok.justcode.value.parameter.SingleParameter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

class SourceClass(
    val jar: JarTranslator,
    val node: ClassNode,
) {
    var maxFields = 0
    val methods = HashMap<String, SourceMethod>()
    val handlers = ArrayList<Trigger>()
    val onInit = ArrayList<JustOperation>()
}