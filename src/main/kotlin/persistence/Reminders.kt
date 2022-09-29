package tech.grimm.midgard.persistence

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Reminders : IntIdTable() {
    var user = varchar("user", 64) // TODO: 27/09/2022 snowflakes are 64bit not 30characters smh
    var guild = varchar("guild", 64) // TODO: 27/09/2022 snowflakes are 64bit not 30characters smh
    var channel = varchar("channel", 64) // TODO: 27/09/2022 snowflakes are 64bit not 30characters smh
    var isPublic = bool("isPublic")
    var created: Column<LocalDateTime> = datetime("created")
    var expires = datetime("expires")
    var text = text("text")
}