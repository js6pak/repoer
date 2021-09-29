package com.aliucord.repoer

import com.aliucord.repoer.data.SemanticVersionSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.skuzzle.semantic.Version
import java.io.Reader

val gson: Gson = GsonBuilder()
    .registerTypeAdapter(Version::class.java, SemanticVersionSerializer())
    .create()

inline fun <reified T> Gson.fromJson(json: String): T {
    return this.fromJson(json, T::class.java)
}

inline fun <reified T> Gson.fromJson(reader: Reader): T {
    return this.fromJson(reader, T::class.java)
}
