package me.unidok.justcode.value

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

data class GameValue(
    val id: String,
    val selector: Selector? = null
) : Value {
    enum class Selector {
        CURRENT,
        DEFAULT,
        DEFAULT_ENTITY,
        KILLER,
        DAMAGER,
        VICTIM,
        SHOOTER,
        PROJECTILE,
        LAST_ENTITY
    }

    override fun serialize(): JsonObject = JsonObject(mapOf(
        "type" to JsonPrimitive("game_value"),
        "game_value" to JsonPrimitive(id),
        "selection" to JsonPrimitive(selector?.let {
            JsonObject(mapOf("type" to JsonPrimitive(it.name.lowercase())))
        }.toString())
    ))

    override fun toString(): String {
        return if (selector == null) "%val($id)"
        else "%val($id,$selector)"
    }
}