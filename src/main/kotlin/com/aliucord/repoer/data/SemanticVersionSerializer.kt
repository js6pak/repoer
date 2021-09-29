package com.aliucord.repoer.data

import com.google.gson.*
import de.skuzzle.semantic.Version
import java.lang.reflect.Type

class SemanticVersionSerializer : JsonSerializer<Version?>, JsonDeserializer<Version?> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Version {
        return Version.parseVersion(json.asString)
    }

    override fun serialize(src: Version?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
