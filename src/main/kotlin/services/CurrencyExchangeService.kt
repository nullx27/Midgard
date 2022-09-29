package tech.grimm.midgard.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import me.jakejmattson.discordkt.annotations.Service

@Service
class CurrencyExchangeService(private val httpClientService: HttpClientService) {
    private val exchangeApiUrl = "https://api.exchangerate.host/latest";

    fun getExchangeRate(amount: Double, base: String, target: String): ExchangeRate {

        val exchangeRate: ExchangeRate
        runBlocking {
            val client = httpClientService.getClient()

            exchangeRate = client.get(exchangeApiUrl) {
                url {
                    parameters.append("base", base.uppercase())
                    parameters.append("symbols", target.uppercase())
                    parameters.append("amount", amount.toString())
                }
            }.body()
        }

        return exchangeRate;
    }
}

@kotlinx.serialization.Serializable
data class ExchangeRate(
    val motd: Map<String, String>,
    val base: String,
    val date: LocalDate,
    val rates: Map<String, Double>
)