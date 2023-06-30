package tech.grimm.midgard.commands

import dev.kord.core.behavior.interaction.response.respond
import dev.kord.rest.builder.message.modify.embed
import me.jakejmattson.discordkt.arguments.AnyArg
import me.jakejmattson.discordkt.commands.subcommand
import me.jakejmattson.discordkt.extensions.addField
import tech.grimm.midgard.services.OpenAiService

fun openai(openAiService: OpenAiService) = subcommand("OpenAI") {
    sub("Chat", "Talk to ChatGPT") {
        execute(
            AnyArg("Message", "Chat Text"),
            AnyArg(
                "Instructions",
                "Instruct the chat how to respond, leave empty if you don't know what you're doing!"
            ).optional("You are a helpful assistant that gives short and precise answers.")
        ) {


            val responder = this.interaction?.deferPublicResponse()
            val response = openAiService.sendChatMessage(args.first, args.second)

            if (response == null) {
                responder?.respond { content = "Error, try again" }
                return@execute
            }

            responder?.respond {
                // respect the field character limit and make sure code is rendered properly
                if (response.length < 1024 || !response.contains("```")) {
                    embed {
                        author {
                            name = "OpenAI"
                            url = "https://platform.openai.com"
                            icon = "https://i.imgur.com/zUwTpgB.png"
                        }

                        footer {
                            text = "Reqeusted by ${interaction?.user?.displayName}"
                        }

                        title = "ChatGPT"
                        addField("Prompt", args.first)
                        addField("Answer", response)
                    }
                } else {
                    content = response
                }
            }
        }
    }

    sub("image", "Generate an Image with DALL-E") {
        execute(AnyArg("Prompt", "Prompt to generate the Image")) {

            val responder = this.interaction?.deferPublicResponse()
            val imageUrl = openAiService.generateImage(args.first)

            if (imageUrl == null) {
                responder?.respond { content = "Error, try again" }
                return@execute
            }

            responder?.respond {
                embed {
                    author {
                        name = "OpenAI"
                        url = "https://platform.openai.com"
                        icon = "https://i.imgur.com/zUwTpgB.png"
                    }

                    footer {
                        text = "Reqeusted by ${interaction?.user?.displayName}"
                    }

                    title = "DALL-E"
                    addField("Prompt", args.first)
                    image = imageUrl
                }
            }
        }
    }

}