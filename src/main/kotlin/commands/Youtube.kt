package tech.grimm.midgard.commands

import me.jakejmattson.discordkt.arguments.AnyArg
import me.jakejmattson.discordkt.commands.commands
import tech.grimm.midgard.services.Permissions
import tech.grimm.midgard.services.YoutubeService

fun youtube(youtubeService: YoutubeService) = commands("Media") {

    slash("youtube", "Search a youtube video", Permissions.EVERYONE) {
        execute(AnyArg("Search", "Serach for a youtube video")) {
            val video = youtubeService.search(args.first.toString())

            if (video == null) {
                respond { "No video found for  ${args.first}" }
            } else {
                respondPublic(video.toString())
            }
        }
    }
}