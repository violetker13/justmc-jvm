package me.unidok.jjvm.util

import me.unidok.jjvm.SourceMethod
import me.unidok.jjvm.TranslationContext
import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.OperationResult
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

object Translator {
    const val RETURN_VARIABLE_NAME = "res"
    val ADDRESS_VARIABLE = Variable("ADR")
    val RETURN_VARIABLE = Variable(RETURN_VARIABLE_NAME, Variable.Scope.LINE)
    val RETURN_KEY = TextValue(RETURN_VARIABLE_NAME)
    val LINE_NUMBER_VARIABLE = Variable("ln", Variable.Scope.LOCAL)

    private val incrementAddress = JustOperation(
        "set_variable_increment", mapOf(
            "variable" to ADDRESS_VARIABLE
        )
    )

    const val INLINE_METHOD_LABEL_NAME = "i"
    val INLINE_METHOD_LABEL = TextValue(INLINE_METHOD_LABEL_NAME)

    private val returnMethod = JustOperation("control_return_function")
    private val returnInline = JustOperation("control_stop_repeat") //JustOperation("control_break_label", mapOf("label" to INLINE_METHOD_LABEL))

    fun returnMethod(context: TranslationContext): JustOperation {
        return if (context.varOffset > 0) returnInline else returnMethod
    }

    fun static(owner: String, name: String): Variable = Variable("$owner.$name")
    fun argumentKey(n: Int): TextValue = TextValue(localName(n))
    fun localName(n: Int): String = "#$n"
    fun instance(address: Value): Variable = instance(address.toString())
    fun classInstance(a: Value): Variable = classInstance("%entry($a,class)")
    fun classInstance(className: String): Variable = instance("Class<$className>")
    fun instance(address: String): Variable = Variable(address)

    fun setVariable(variable: Variable, value: Value): JustOperation = JustOperation(
        "set_variable_value", mapOf(
            "variable" to variable,
            "value" to value
        )
    )

    fun setVariable(context: TranslationContext, variable: Variable?, value: Operand): Value {
        if (value is OperationResult) return value.translate(context, variable)
        val value = value.translate(context, null)
        if (variable != null && variable != value) {
            context.addOperation(setVariable(variable, value))
            return variable
        }
        return value
    }

    fun newInstance(context: TranslationContext, variable: Variable?): Variable {
        val variable = variable ?: context.tempVar()
        context.addOperation(incrementAddress)
        context.addOperation(setVariable(variable, ADDRESS_VARIABLE))
        return variable
    }
}