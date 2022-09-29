package tech.grimm.midgard.commands

import me.jakejmattson.discordkt.arguments.AnyArg
import me.jakejmattson.discordkt.arguments.BooleanArg
import me.jakejmattson.discordkt.arguments.ChoiceArg
import me.jakejmattson.discordkt.arguments.IntegerArg
import me.jakejmattson.discordkt.commands.subcommand
import me.jakejmattson.discordkt.extensions.addField
import me.jakejmattson.discordkt.extensions.addInlineField
import tech.grimm.midgard.persistence.Reminders
import tech.grimm.midgard.services.ReminderService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun remindMe(reminderService: ReminderService) = subcommand("RemindMe") {
    sub("New", "Create a new Reminder") {
        execute(
            AnyArg("Reminder", "Reminder Text"),
            IntegerArg("Amount", "Amount of time"),
            ChoiceArg("Unit", "Unit of Time", "minutes", "hours", "days", "weeks"),
        ) {

            val expires: LocalDateTime = when (args.third) {
                "minutes" -> LocalDateTime.now().plusMinutes(args.second.toLong())
                "hours" -> LocalDateTime.now().plusHours(args.second.toLong())
                "days" -> LocalDateTime.now().plusDays(args.second.toLong())
                "weeks" -> LocalDateTime.now().plusWeeks(args.second.toLong())
                else -> LocalDateTime.now()
            }

            reminderService.add(author, guild, channel, expires, args.first)

            val response =
                "Reminder with text \"${args.first}\" for ${expires.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))} successfully created!"
            respond(response)
        }
    }

    sub("List", "List all my active reminders") {
        execute {
            val reminders = reminderService.getByAuthorAndGuild(author, guild)

            respond {
                title = "My Reminders"
                reminders.forEach {
                    addInlineField("id", it[Reminders.id].toString())
                    addInlineField(
                        "created",
                        it[Reminders.created].format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
                    )
                    addInlineField(
                        "expires",
                        it[Reminders.expires].format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
                    )
                    addField("reminder", it[Reminders.text])
                }
            }

        }
    }

    sub("Remove", "Remove a reminder") {

        execute(IntegerArg("ID")) {
            reminderService.remove(args.first, author, guild)
            respond("Removed reminder with id ${args.first}")

        }
    }
}