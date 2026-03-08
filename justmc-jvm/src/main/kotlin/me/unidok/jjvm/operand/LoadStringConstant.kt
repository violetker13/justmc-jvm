package me.unidok.jjvm.operand

import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operation.New
import me.unidok.jjvm.operation.PutField
import me.unidok.jjvm.operation.StoreToConstantPool
import me.unidok.jjvm.util.Values
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class LoadStringConstant(
    val value: String
) : Operand {
    override fun translate(context: TranslationContext, variable: Variable?): Value {
        val jar = context.sourceMethod.sourceClass.jar
        val constants = jar.dynamicConstants
        return constants[value] ?: Variable("s${constants.size}").also { variable ->
            constants[value] = variable
            val new = New("java/lang/String")
            val adr = OperationResult(new)
            val store = StoreToConstantPool(variable.name, adr)
            val putField =
                PutField("java/lang/String", "value", "Ljustmc/Text;", adr, NativeValue(Values.valueOf(value)))
            jar.registerNatives.addAll(listOf(new, store, putField))
        }
    }
}