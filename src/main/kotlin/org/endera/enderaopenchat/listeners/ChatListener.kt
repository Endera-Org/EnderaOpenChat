package org.endera.enderaopenchat.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.endera.enderalib.adventure.componentToString
import org.endera.enderalib.adventure.stringToComponent
import org.endera.enderalib.utils.async.runTask
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.config.resolveChannel
import org.endera.enderaopenchat.utils.cparse
import org.endera.enderaopenchat.utils.isPlayerVanished
import org.endera.enderaopenchat.utils.nearbyPlayers
import org.endera.enderaopenchat.utils.papiParse

@Suppress("unused")
class ChatListener : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerChatSent(event: AsyncChatEvent) {
        val config = EnderaOpenChat.config
        val player = event.player
        val (channel, stringMessage) = config.resolveChannel(event.message().componentToString()) ?: return

        if (channel.usePermission && !player.hasPermission("echat.${channel.name}.send")) {
            player.sendMessage(config.messages.nochannelpermission.cparse())
            event.isCancelled = true
            return
        }

        val senderIsVanished = isPlayerVanished(player)
        val isRanged = channel.range > 0

        val candidatePlayers = when {
            channel.range == -2 -> Bukkit.getOnlinePlayers()
            channel.range == -1 -> player.world.players
            isRanged -> nearbyPlayers(player, player.world.players, channel.range)
            else -> listOf(player)
        }

        val viewers = candidatePlayers.filter { potentialViewer ->
            if (potentialViewer == player) return@filter true

            if (channel.usePermission && !potentialViewer.hasPermission("echat.${channel.name}.view")) {
                return@filter false
            }

            !senderIsVanished || isPlayerVanished(potentialViewer)
        }

        if (isRanged && !senderIsVanished && viewers.none { it != player && !isPlayerVanished(it) }) {
            player.runTask(EnderaOpenChat.instance) {
                player.sendActionBar(config.messages.localnoone.cparse())
            }
        }

        val eViewers = event.viewers()

        eViewers.clear()
        eViewers.addAll(viewers)
        eViewers.add(Bukkit.getConsoleSender())

        event.renderer { _, _, message, _ ->
            message
        }

        // The player's message goes in last so placeholders typed in chat are never resolved
        event.message(
            channel.format
                .replace("{player}", player.name)
                .papiParse(player)
                .replace("{message}", stringMessage)
                .stringToComponent()
        )
    }
}
