package org.endera.enderaopenchat.utils

import org.bukkit.entity.Player

fun nearbyPlayers(player: Player, otherPlayers: List<Player>, range: Int): List<Player> {
    val playerLocation = player.location
    val rangeSquared = range * range

    return otherPlayers.filter { other ->
        other.world == playerLocation.world &&
                playerLocation.distanceSquared(other.location) <= rangeSquared
    }
}