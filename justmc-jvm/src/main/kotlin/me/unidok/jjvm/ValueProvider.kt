package me.unidok.jjvm

import me.unidok.jjvm.operand.Operand
import me.unidok.jjvm.operand.OperationResult
import me.unidok.jjvm.util.JustOperation
import me.unidok.justcode.value.TextValue
import me.unidok.justcode.value.Value
import me.unidok.justcode.value.Variable

data class ValueProvider(
    val returnVariable: Variable = defaultReturnVariable,
    var tempVars: Int = 0,
    val varOffset: Int = 0,
) {
    companion object {
        /** Текущий адрес для аллокатора */
        val addressVariable = Variable("adr")
        val defaultReturnVariable = Variable("res", Variable.Scope.LINE)
        val lineNumberVariable = Variable("ln", Variable.Scope.LOCAL)
        private val inlineMethodLabel = TextValue("i")
        private val returnFunction = JustOperation("control_return_function")
        private val returnInline = JustOperation("control_break_label", mapOf("label" to inlineMethodLabel))
        private val incrementAddressOperation =
            JustOperation("set_variable_increment", mapOf("variable" to addressVariable))

        /** Локальная переменная, использующаяся для трансляции стековой машины в адресную */
        fun tempVar(n: Int): Variable = Variable("t$n", Variable.Scope.LINE)

        /** Локальная переменная */
        fun localVar(n: Int): Variable = Variable("#$n", Variable.Scope.LINE)

        /** Глобальная переменная */
        fun staticVar(owner: String, name: String): Variable = Variable("$owner.$name")

        /** Переменная, хранящая экземпляр класса (список из чисел, который имеет фиксированный размер) */
        fun instance(address: Value): Variable = Variable("$address")

        /** Переменная, хранящая таблицу виртуальных методов для класса (текст=текст) */
        fun vtable(classAddress: Variable) = Variable("${classAddress.name}_vtable")

        fun setVariable(variable: Variable, value: Value): JustOperation = JustOperation(
            "set_variable_value", mapOf(
                "variable" to variable,
                "value" to value
            )
        )

        fun setVariable(context: TranslationContext, variable: Variable?, value: Operand): Value {
            if (value is OperationResult) return value.translate(context, variable)
            return setVariable(context, variable, value.translate(context, null))
        }

        fun setVariable(context: TranslationContext, variable: Variable?, value: Value): Value {
            if (variable != null && variable != value) {
                context.addOperation(setVariable(variable, value))
                return variable
            }
            return value
        }

        fun newInstance(context: TranslationContext, variable: Variable?): Variable {
            val variable = variable ?: context.tempVar()
            context.addOperation(incrementAddressOperation)
            context.addOperation(setVariable(variable, addressVariable))
            return variable
        }
    }

    val returnOperation: JustOperation
        get() = if (varOffset > 0) returnInline else returnFunction

    fun tempVar(): Variable = tempVar(tempVars++)
    fun localVar(n: Int): Variable = Companion.localVar(n + varOffset)

}