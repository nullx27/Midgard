package tech.grimm.midgard.persistence

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

object Reminders : IntIdTable() {
    var user = varchar("user", 64)
    var guild = varchar("guild", 64)
    var channel = varchar("channel", 64)
    var created: Column<LocalDateTime> = datetime("created")
    var expires = datetime("expires")
    var text = text("text")
}