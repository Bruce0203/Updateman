package io.github.bruce0203.updateman

import com.rylinaux.plugman.util.PluginUtil
import org.bukkit.plugin.Plugin
import org.eclipse.jgit.api.Git
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.concurrent.Executors
import java.util.function.Consumer


class Update(
    plugin: Plugin,
    gitURL: String,
    dir: File,
    cmd: String,
    out: String) {

    init {

        val git: Git = if (dir.exists()) {
            Git.open(File(dir, ".git"))
        } else {
            Git.cloneRepository()
                .setURI(gitURL)
                .setDirectory(dir)
                .call()
        }
        if (git.pull().call().fetchResult.trackingRefUpdates.isNotEmpty()) {
            val isWindows = System.getProperty("os.name")
                .lowercase(Locale.getDefault()).startsWith("windows")


            val builder = ProcessBuilder()
            if (isWindows) {
                builder.command("cmd.exe", "/c", cmd)
            } else {
                builder.command("sh", "-c", cmd)
            }
            builder.directory(dir)
            val process = builder.start()
            val streamGobbler = StreamGobbler(process.inputStream, System.out::println)
            val future = Executors.newSingleThreadExecutor().submit(streamGobbler)
            println("Updating...")
            future.get().toString().apply(::println)
            println("Update Done!")

            Files.copy(File(dir, out).toPath(), File("plugins/update").toPath(), StandardCopyOption.REPLACE_EXISTING)
            PluginUtil.reload(plugin)

        }
    }

    private class StreamGobbler(private val inputStream: InputStream, val consumer: Consumer<String>) : Runnable {
        override fun run() {
            BufferedReader(InputStreamReader(inputStream)).lines()
                .forEach(consumer)
        }
    }

}