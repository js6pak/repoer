package com.aliucord.repoer.task

import com.aliucord.repoer.data.*
import com.aliucord.repoer.fromJson
import com.google.gson.GsonBuilder
import de.skuzzle.semantic.Version
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.ByteArrayInputStream
import java.net.URL
import java.util.*
import java.util.zip.ZipInputStream

abstract class ImportTask : DefaultTask() {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Version::class.java, SemanticVersionSerializer())
        .setPrettyPrinting()
        .create()

    @get:Input
    @set:Option(option = "url", description = "Plugin download url")
    abstract var url: String

    private fun extractManifest(bytes: ByteArray): PluginManifest {
        var manifest: PluginManifest? = null

        ZipInputStream(ByteArrayInputStream(bytes)).use {
            for (zipEntry in generateSequence { it.nextEntry }) {
                if (zipEntry.name == "manifest.json") {
                    manifest = this.gson.fromJson<PluginManifest>(it.reader())
                }
            }
        }

        requireNotNull(manifest) {
            "Couldn't find the manifest.json file"
        }

        return manifest!!
    }

    @TaskAction
    fun import() {
        val bytes = URL(url).openStream().readAllBytes()

        val hash = DigestUtils.sha1Hex(bytes)
        val manifest = extractManifest(bytes)

        val jsonFile = project.file("plugins").resolve(manifest.name + ".json")

        val versions =
            if (jsonFile.exists()) gson.fromJson<FullPluginInfo>(jsonFile.readText()).versions
            else TreeMap()

        versions[manifest.version] = VersionInfo(
            if (manifest.changelog == null) null else ChangelogInfo(
                manifest.changelog,
                manifest.changelogMedia
            ), DownloadInfo(url, hash)
        )

        versions.comparator()

        jsonFile.writeText(
            gson.toJson(
                FullPluginInfo(
                    manifest.name,
                    manifest.description,
                    manifest.authors,
                    manifest.links ?: HashMap(),
                    versions
                )
            ) + "\n"
        )
    }
}