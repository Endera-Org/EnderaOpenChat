package org.endera.enderaopenchat.utils

import org.bukkit.entity.Player
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.Integrations
import org.endera.enderaopenchat.integrations.CMIVanishHook

fun isPlayerVanished(player: Player): Boolean {
    val isCmi = EnderaOpenChat.integrations[Integrations.CMI] != null
    return if (isCmi) {
        CMIVanishHook.isPlayerVanished(player)
    } else {
        player.getMetadata("vanished").any { it.asBoolean() }
    }
}