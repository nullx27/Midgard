package tech.grimm.midgard.services

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import me.jakejmattson.discordkt.annotations.Service
import tech.grimm.midgard.data.Configuration

@Service
class YoutubeService(private val configuration: Configuration) {

    private fun youtube(): YouTube {
        val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return YouTube.Builder(httpTransport, GsonFactory.getDefaultInstance(), null)
            .setApplicationName("Midgard Discord Bot") // TODO: 27/09/2022 this needs to come out of the config file
            .build()
    }


    fun search(term: String): String? {
        val service = youtube();

        val request: YouTube.Search.List = service.search().list(listOf())
        val response: SearchListResponse = request
            .setKey(configuration.apis.youtube)
            .setQ(term)
            .setType(listOf("video"))
            .setSafeSearch("none")
            .setChannelType("any")
            .setOrder("relevance")
            .setVideoEmbeddable("true")
            .execute();

        if (response.items.isEmpty()) return null

        return buildYoutubeUrl(response.items.first().id.videoId)
    }

    private fun buildYoutubeUrl(videoId: String): String {
        return "https://www.youtube.com/watch?v=$videoId"
    }
}