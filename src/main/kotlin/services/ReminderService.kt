package tech.grimm.midgard.services

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.asChannelOf
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Guild
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.allowedMentions
import dev.kord.rest.builder.message.embed
import kotlinx.coroutines.runBlocking
import me.jakejmattson.discordkt.Discord
import me.jakejmattson.discordkt.annotations.Service
import me.jakejmattson.discordkt.util.addField
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.inList
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.less
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import tech.grimm.midgard.persistence.Reminders
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Timer
import kotlin.concurrent.timerTask
import org.jetbrains.exposed.v1.jdbc.*

@Service
class ReminderService(private val discord: Discord) {

    init {
        Timer().scheduleAtFixedRate(timerTask {
            val reminders = this@ReminderService.getExpiredReminders()
            runBlocking {
                reminders.forEach {
                    sendReminder(it)
                }
            }
        }, 10000, 2000)
    }

    suspend fun add(
        aUser: User,
        aGuild: Guild,
        aChannel: MessageChannel,
        expiresAt: LocalDateTime,
        aText: String,
    ) {
        runBlocking {
            transaction {
                Reminders.insert {
                    it[user] = aUser.id.toString()
                    it[guild] = aGuild.id.toString()
                    it[channel] = aChannel.id.toString()
                    it[created] = LocalDateTime.now()
                    it[expires] = expiresAt
                    it[text] = aText
                }
            }
        }
    }

    suspend fun getByAuthorAndGuild(aUser: User, aGuild: Guild): List<ResultRow> = transaction {
        Reminders.select(Reminders.user.eq(aUser.id.toString()) and (Reminders.guild.eq(aGuild.id.toString())))
            .toList()
    }

    suspend fun remove(id: Int, aUser: User, aGuild: Guild) = transaction {
        Reminders.deleteWhere { Reminders.id eq id and (Reminders.user eq aUser.id.toString()) and (Reminders.guild eq aGuild.id.toString()) }
    }

    private fun getExpiredReminders(): List<ResultRow> {
        val reminders = transaction {
            Reminders.select(Reminders.expires less LocalDateTime.now()).toList()
        }

        transaction {
            val ids = reminders.map { it[Reminders.id] }
            Reminders.deleteWhere { Reminders.id inList ids }
        }

        return reminders
    }


    private suspend fun sendReminder(reminder: ResultRow) {
        discord.kord.getChannel(Snowflake(reminder[Reminders.channel]))?.asChannelOf<TextChannel>()
            ?.createMessage {
                allowedMentions {
                    users.add(Snowflake(reminder[Reminders.user]))
                }

                content = "<@${reminder[Reminders.user]}>"

                embed {
                    title = "Reminder"
                    description = reminder[Reminders.text]
                    addField(
                        "Created",
                        reminder[Reminders.created].format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            .toString()
                    )
                }
            }
    }
}