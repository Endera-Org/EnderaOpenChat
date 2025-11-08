package org.endera.enderaopenchat.utils

import org.bukkit.entity.Player

fun nearbyPlayers(centerPlayer: Player, otherPlayers: Collection<Player>, range: Int): List<Player> {
    val playerLocation = centerPlayer.location
    val rangeSquared = range * range

    return otherPlayers.filter { other ->
        if (other == centerPlayer) return@filter true

        other.world == playerLocation.world &&
                playerLocation.distanceSquared(other.location) <= rangeSquared
    }
}