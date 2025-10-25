package org.endera.enderaopenchat.utils

import org.bukkit.entity.Player
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.Integrations
import org.endera.enderaopenchat.integrations.CMIVanishHook

fun nearbyPlayers(centerPlayer: Player, otherPlayers: Collection<Player>, range: Int): List<Player> {
    val playerLocation = centerPlayer.location
    val rangeSquared = range * range
    val isCmi = EnderaOpenChat.integrations[Integrations.CMI] != null

    return otherPlayers.filter { other ->

        val isVanished = if (isCmi) {
            CMIVanishHook.isPlayerVanished(other)
        } else {
            other.hasMetadata("vanished") && other.getMetadata("vanished").any { it.asBoolean() }
        }

        if (isVanished) {
            return@filter false
        }

        other.world == playerLocation.world &&
                playerLocation.distanceSquared(other.location) <= rangeSquared
    }
}