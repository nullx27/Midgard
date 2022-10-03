package tech.grimm.midgard

import dev.kord.common.annotation.KordPreview
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import dev.kord.x.emoji.Emojis
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.jakejmattson.discordkt.dsl.CommandException
import me.jakejmattson.discordkt.dsl.ListenerException
import me.jakejmattson.discordkt.dsl.bot
import me.jakejmattson.discordkt.locale.Language
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tech.grimm.midgard.data.Configuration
import tech.grimm.midgard.lib.scheduler.Scheduler
import tech.grimm.midgard.persistence.Reminders
import tech.grimm.midgard.services.Permissions
import java.awt.Color

@OptIn(PrivilegedIntent::class)
@KordPreview
suspend fun main() {
    runBlocking {
        launch {

            bot(System.getenv("DISCORD_TOKEN")) {
                val configuration = data("data/config.json") { Configuration() }

                prefix { configuration.prefix }

                configure {
                    mentionAsPrefix = true
                    logStartup = true
                    documentCommands = true
                    recommendCommands = true
                    searchCommands = true
                    deleteInvocation = true
                    dualRegistry = false
                    commandReaction = Emojis.eyes
                    theme = Color(0xe9a80f)
                    intents = Intents.privileged
                    defaultPermissions = Permissions.EVERYONE
                }

                onException {
                    if (exception is java.lang.IllegalArgumentException) return@onException

                    when (this) {
                        is CommandException -> println("Exception '${exception::class.simpleName}' in command ${event.command?.name}")
                        is ListenerException -> println("Exception '${exception::class.simpleName}' in listener ${event::class.simpleName}")
                    }
                }

                presence { watching("you!") }

                onStart {
                    val guilds = kord.guilds.toList()
                    println("Guilds: ${guilds.joinToString { it.name }}")

                    Database.connect("jdbc:sqlite:${configuration.database}", "org.sqlite.JDBC")
                    transaction { SchemaUtils.create(Reminders) }
                }

                localeOf(Language.EN) {
                    helpName = "Help"
                    helpCategory = "Utility"
                    commandRecommendation = "Recommendation: {0}"
                }

                inject(Scheduler())
            }
        }
    }
}