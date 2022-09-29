package tech.grimm.midgard.commands

import me.jakejmattson.discordkt.arguments.AnyArg
import me.jakejmattson.discordkt.commands.commands
import me.jakejmattson.discordkt.extensions.addField
import tech.grimm.midgard.services.Permissions
import tech.grimm.midgard.services.WolframAlphaService

fun wolframAlpha(wolframAlphaService: WolframAlphaService) = commands("Utility") {
    slash("WolframAlpha", "Ask WolframAlpha something", Permissions.EVERYONE) {
        execute(AnyArg("Input", "Input")) {
            val result = wolframAlphaService.execute(args.first)

            if (!result.queryresult.success) {
                respond("No Result")
                return@execute
            }

            respondPublic {
                author {
                    name = "Wolfram|Alpha"
                    url = "https://www.wolframalpha.com/"
                    icon = "https://i.imgur.com/YVWvjlM.png"
                }

                result.queryresult.pods.forEach {
                    when (it.id) {
                        "Input", "Result" -> addField(it.title, it.subpods.first().plaintext)
                        "Plot" -> image = it.subpods.first().img.src
                    }
                }
            }
        }
    }
}