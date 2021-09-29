package com.aliucord.repoer.data

import de.skuzzle.semantic.Version

data class PluginManifest(
    val pluginClassName: String,
    val name: String,
    val version: Version,
    val description: String,
    val authors: List<AuthorInfo>,
    val links: HashMap<String, String>?,
    val changelog: String?,
    val changelogMedia: String?
)