package io.github.bruce0203.updateman

import org.eclipse.jgit.api.Git
import java.io.File

fun pull(destiny: String) {
    Git.open(File(destiny))
        .pull().call().fetchResult.apply {
            println(this.messages)
        }
}