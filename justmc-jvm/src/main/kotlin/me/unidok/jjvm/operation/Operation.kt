package me.unidok.jjvm.operation

import me.unidok.jjvm.TranslationContext

interface Operation {
    val length: Int get() = 1

    fun translate(context: TranslationContext)
}