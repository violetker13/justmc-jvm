package me.unidok.jjvm.operation

abstract class MethodOperation : OperationWithResult() {
    abstract val owner: String
    abstract val name: String
    abstract val desc: String
    open val fullName: String get() = "$owner.$name$desc"
}