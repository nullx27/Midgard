package tech.grimm.midgard.services

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import me.jakejmattson.discordkt.annotations.Service
import kotlin.random.Random

@Service
class BoobaService(private val httpClientService: HttpClientService) {

    private val uri = "https://api.booba.tv/";

    private fun getEntries(): List<BoobaResponse> {
        val client = httpClientService.getClient()
        val response: List<BoobaResponse>;

        runBlocking {
            response = client.get(uri).body()
        }

        return response;
    }

    fun getTopEntry() = getEntries().maxByOrNull { it.stream_viewer_count }!!
    fun getRandomEntry() = getEntries().random()
}

@kotlinx.serialization.Serializable
data class BoobaResponse(
    val user_id: String,
    val user_login: String,
    val user_broadcaster_type: String,
    val user_display_name: String,
    val user_profile_image_url: String,
    val user_offline_image_url: String,
    val stream_id: String,
    val stream_game_id: String,
    val stream_game_name: String,
    val stream_title: String,
    val stream_viewer_count: String,
    val stream_thumbnail_url: String
)