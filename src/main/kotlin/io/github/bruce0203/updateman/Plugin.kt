package io.github.bruce0203.updateman

import io.github.monun.kommand.kommand
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("unused")
class Plugin : JavaPlugin() {

    override fun onEnable() {
        kommand {
            register("updateman") {
                config.getKeys(false).forEach { key ->
                    then(key) {
                        executes {
                            val section = config.getConfigurationSection(key)!!
                            val plugin = Bukkit.getPluginManager().getPlugin(section.getString("plugin")!!)!!
                            Update(
                                plugin,
                                section.getString("url")!!,
                                File(dataFolder, plugin.name),
                                section.getString("cmd")!!,
                                section.getString("out")!!,
                            )
                        }
                    }

                }
            }
        }
    }

}