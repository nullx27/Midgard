package tech.grimm.midgard.tasks

import me.jakejmattson.discordkt.Discord
import tech.grimm.midgard.lib.scheduler.schedule

fun presenceTasks() = schedule("Presence Tasks") {

    task("update", 2000) {
        execute {
            println("test")
        }
    }
}