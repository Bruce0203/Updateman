package io.github.bruce0203.updateman

import io.github.brucefreedy.mccommand.MCCommand
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("unused")
class Plugin : JavaPlugin() {

    override fun onEnable() {
        MCCommand(this) {
            command("updateman") {
                config.getKeys(false).forEach { key ->
                    then(key) {
                        execute {
                            val section = config.getConfigurationSection(key)!!
                            val plugin = Bukkit.getPluginManager().getPlugin(section.getString("plugin")!!)
                            if (section.getBoolean("download")) {
                                download(
                                    section.getString("plugin")!!,
                                    section.getString("url")!!,
                                    section.getString("destiny")!!,
                                    )
                            } else Update(
                                plugin?: this@Plugin,
                                section.getString("plugin")!!,
                                section.getString("url")!!,
                                File(dataFolder, key),
                                section.getString("cmd")!!,
                                section.getString("out")!!,
                                section.getString("branch")!!,
                            )
                        }
                    }

                }
            }
        }
    }

}