/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.aliucord.repoer.task

import com.aliucord.repoer.data.CompiledPluginInfo
import com.aliucord.repoer.data.FullPluginInfo
import com.aliucord.repoer.fromJson
import com.aliucord.repoer.gson
import org.gradle.api.Project

const val TASK_GROUP = "aliucord"

fun registerTasks(project: Project) {
    project.tasks.register("compile") {
        it.group = TASK_GROUP

        val plugins = ArrayList<CompiledPluginInfo>()

        val pluginsDirectory = project.buildDir.resolve("plugins")
        pluginsDirectory.mkdirs()

        project.fileTree("plugins").forEach {
            val plugin = gson.fromJson<FullPluginInfo>(it.readText())

            pluginsDirectory.resolve(plugin.name).mkdir()

            val file = pluginsDirectory.resolve(plugin.name + ".json")
            file.writeText(gson.toJson(plugin))

            plugins.add(CompiledPluginInfo(plugin))
        }

        project.buildDir.resolve("plugins.json").writeText(gson.toJson(plugins))
    }

    project.tasks.register("import", ImportTask::class.java) {
        it.group = TASK_GROUP
    }
}