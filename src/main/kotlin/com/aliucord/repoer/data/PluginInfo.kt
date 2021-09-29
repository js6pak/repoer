package com.aliucord.repoer.data

import de.skuzzle.semantic.Version
import java.util.*

abstract class BasePluginInfo {
    abstract val name: String
    abstract val description: String
    abstract val authors: List<AuthorInfo>
    abstract val links: HashMap<String, String>
}

data class FullPluginInfo(
    override val name: String,
    override val description: String,
    override val authors: List<AuthorInfo>,
    override val links: HashMap<String, String>,
    val versions: TreeMap<Version, VersionInfo>
) : BasePluginInfo()

data class CompiledPluginInfo(
    override val name: String,
    override val description: String,
    override val authors: List<AuthorInfo>,
    override val links: HashMap<String, String>,
    val version: Version,
    val changelog: ChangelogInfo?,
    val download: DownloadInfo
) : BasePluginInfo() {
    constructor(original: FullPluginInfo) : this(
        original.name,
        original.description,
        original.authors,
        original.links,
        original.versions.keys.maxByOrNull { it }!!,
        original.versions.maxByOrNull { it.key }!!.value.changelog,
        original.versions.maxByOrNull { it.key }!!.value.download
    )
}

data class AuthorInfo(
    val id: Long,
    val name: String
)

data class VersionInfo(
    val changelog: ChangelogInfo?,
    val download: DownloadInfo
)

data class ChangelogInfo(
    val text: String,
    val media: String?
)

data class DownloadInfo(
    val url: String,
    val sha1: String
)
