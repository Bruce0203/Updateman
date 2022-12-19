package io.github.bruce0203.updateman

import com.rylinaux.plugman.util.PluginUtil
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import java.io.File
import java.net.URL

fun download(pluginName: String, url: String, destiny: String) {
    val destinyFile = File(destiny)
    println("Downloading ${destinyFile.name}")
    FileUtils.copyURLToFile(URL(url), destinyFile)
    println("Downloaded  ${destinyFile.name}")
    val pl = Bukkit.getPluginManager().getPlugin(pluginName)
    if (pl == null) PluginUtil.load(pl)
    else PluginUtil.reload(pl)
}