package me.unidok.jjvm.model

import kotlinx.serialization.Serializable

@Serializable
data class JJVMConfig(
    val exclude: List<String> = emptyList(),
    val includeUnused: Boolean = true,
    val maxInlineLength: Int = 3,
    //val sourceLineNumbers: Boolean = false,
    //val exceptionStackTrace: Boolean = false,
    val printDebug: Boolean = true,
    val prettyOutput: Boolean = true,
    val loadForce: Boolean = true,
)