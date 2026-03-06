package me.unidok.justcode.value

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

data class MapValue(
    val values: Map<Value, Value>
) : Value {
    override fun serialize(): JsonObject = JsonObject(mapOf(
        "type" to JsonPrimitive("map"),
        "values" to JsonObject(values.entries.associate {
            it.key.serialize().toString() to it.value.serialize()
        })
    ))
}