package me.unidok.jjvm.model

import kotlinx.serialization.Serializable

@Serializable
data class JJVMConfig(
    val independent: Boolean = true,
    val exclude: List<String> = emptyList(),
    val skipJarDebug: Boolean = false,
    val inlineActionsUntil: Int = 5,
    val sourceLineNumbers: Boolean = false,
    val exceptionStackTrace: Boolean = false,
    val debug: Boolean = false,
    val prettyOutput: Boolean = false,
)