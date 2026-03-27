package me.unidok.jjvm.operation

import me.unidok.jjvm.context.TranslationContext

abstract class Operation {
    open val length: Int get() = 1

    abstract fun translate(context: TranslationContext)

    abstract fun appendTo(builder: StringBuilder, indent: String)

    override fun toString(): String = buildString { appendTo(this, "") }
}