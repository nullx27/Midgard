package tech.grimm.midgard.data

import dev.kord.common.entity.Snowflake
import me.jakejmattson.discordkt.dsl.Data

@kotlinx.serialization.Serializable
data class Configuration(
    val owner: Snowflake = Snowflake(0),
    val database: String = "data/database.db",
    val apis: APIs = APIs(),
    val prefix: String = "!"
) : Data()


@kotlinx.serialization.Serializable
data class APIs(val youtube: String = "", val wolframAlpha: String = "", val openAI: String = "")