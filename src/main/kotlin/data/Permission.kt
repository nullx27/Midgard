package tech.grimm.midgard.services

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions

object Permissions {
    val OWNER = Permissions(Permission.ManageGuild)
    val MEMBER = Permissions(Permission.UseApplicationCommands) // TODO: 27/09/2022 this needs to be more precise
    val EVERYONE = Permissions(Permission.UseApplicationCommands)
}