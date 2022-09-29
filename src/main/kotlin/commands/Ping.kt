package tech.grimm.midgard.commands
import me.jakejmattson.discordkt.commands.commands

fun ping() = commands("Utility") {
    slash("ping", "Ping the Bot") {
        execute { respond("Pong!") }
    }
}