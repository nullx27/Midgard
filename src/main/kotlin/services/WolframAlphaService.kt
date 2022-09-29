package tech.grimm.midgard.services

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import me.jakejmattson.discordkt.annotations.Service
import tech.grimm.midgard.data.Configuration
import tech.grimm.midgard.services.models.WolframAlphaResponse

@Service
class WolframAlphaService(private val configuration: Configuration, private val httpClientService: HttpClientService) {

    private val wolframAlphaUrl: String = "https://api.wolframalpha.com/v2/query"

    fun execute(term: String): WolframAlphaResponse {

        val response: WolframAlphaResponse
        runBlocking {
            val client = httpClientService.getClient()
            response = client.get(wolframAlphaUrl) {
                url {
                    parameters.append("input", term)
                    parameters.append("output", "json")
                    parameters.append("appid", configuration.apis.wolframAlpha)
                }
            }.body()
        }

        return response
    }
}