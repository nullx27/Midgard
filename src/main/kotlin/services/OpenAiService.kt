package tech.grimm.midgard.services

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.*
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import me.jakejmattson.discordkt.annotations.Service
import tech.grimm.midgard.data.Configuration
import kotlin.time.Duration.Companion.seconds

@OptIn(BetaOpenAI::class)
@Service
class OpenAiService(private val configuration: Configuration) {
    private val openai = OpenAI(
        token = configuration.apis.openAI,
        timeout = Timeout(socket = 60.seconds)
    )

    suspend fun sendChatMessage(message: String, instruction: String): String? {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-4"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = "${instruction}. Answer in 2000 Characters or less"
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = message
                )
            ),
            n = 1,
            maxTokens = 420 //ayy
        )
        var response: ChatCompletion? = null
        try {
            response = openai.chatCompletion(chatCompletionRequest)
            return response.choices.joinToString { "${it.message?.content}" }

        } catch (e: Exception) {
            return null
        }
    }

    suspend fun generateImage(prompt: String): String? =
        openai.imageURL(
            creation = ImageCreation(
                prompt = prompt,
                n = 1,
                size = ImageSize.is1024x1024
            )
        ).firstOrNull()?.url
}