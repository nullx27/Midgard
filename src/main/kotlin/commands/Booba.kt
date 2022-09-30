package tech.grimm.midgard.commands

import me.jakejmattson.discordkt.arguments.BooleanArg
import me.jakejmattson.discordkt.commands.commands
import me.jakejmattson.discordkt.extensions.addField
import me.jakejmattson.discordkt.extensions.addInlineField
import tech.grimm.midgard.services.BoobaService

fun booba(boobaService: BoobaService) = commands("Fun") {

    slash("Booba", "Get a currently running Booba Stream") {
        execute(BooleanArg("Top").optional(false)) {
            val stream = if (!args.first) boobaService.getRandomEntry() else boobaService.getTopEntry()

            respondPublic {
                author {
                    name = "Booba.tv"
                    url = "https://booba.tv/"
                    icon = "https://booba.tv/img/062f0974c68fad4eb3deaaac6513ea44.gif"
                }

                title = stream.stream_title
                url = "https://www.twitch.tv/${stream.user_display_name}"

                thumbnail {
                    url = stream.stream_thumbnail_url
                        .replace("{width}", "640", true)
                        .replace("{height}", "360", true)
                }

                addInlineField("Streamer", stream.user_display_name)
                addInlineField("Viewer", stream.stream_viewer_count)
                addField("Category", stream.stream_game_name)
            }

        }
    }
}