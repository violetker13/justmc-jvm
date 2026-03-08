package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operation.New
import me.unidok.jjvm.operation.PutField
import me.unidok.jjvm.operation.StoreToConstantPool
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable
import org.objectweb.asm.Type
import kotlin.collections.set

data class LoadTypeConstant(
    val value: Type
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        return context.sourceMethod.sourceClass.jar.getClassAddress(value)
    }
}