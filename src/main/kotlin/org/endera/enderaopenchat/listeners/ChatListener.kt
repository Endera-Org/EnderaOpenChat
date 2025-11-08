package org.endera.enderaopenchat.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.endera.enderalib.adventure.componentToString
import org.endera.enderalib.adventure.stringToComponent
import org.endera.enderalib.utils.async.BukkitRegionDispatcher
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.config.ChatChannel
import org.endera.enderaopenchat.utils.cparse
import org.endera.enderaopenchat.utils.isPlayerVanished
import org.endera.enderaopenchat.utils.nearbyPlayers
import org.endera.enderaopenchat.utils.papiParse

@Suppress("unused")
class ChatListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerChatSent(event: AsyncChatEvent) {
        val config = EnderaOpenChat.config
        val (nonPrefixedChannels, prefixedChannels) = config.channels.partition { it.prefix.isEmpty() }
        val stringMessage = event.message().componentToString()

        val matchedPrefixedChannel = prefixedChannels.firstOrNull { prefixChannel ->
            stringMessage.startsWith(prefixChannel.prefix)
        }

        if (matchedPrefixedChannel != null) {
            val remainder = stringMessage.substring(matchedPrefixedChannel.prefix.length)
            if (remainder.isNotEmpty()) {
                processMessage(event, matchedPrefixedChannel, remainder)
            }
            return
        }

        val defaultChannel = nonPrefixedChannels.firstOrNull() ?: return
        processMessage(event, defaultChannel, stringMessage)
    }

    fun processMessage(
        event: AsyncChatEvent,
        channel: ChatChannel,
        stringMessage: String,
    ) {
        val config = EnderaOpenChat.config
        val player = event.player

        if (channel.usePermission && !player.hasPermission("echat.${channel.name}.send")) {
            player.sendMessage(config.messages.nochannelpermission.cparse())
            event.isCancelled = true
            return
        }

        val senderIsVanished = isPlayerVanished(player)

        val candidatePlayers = when (channel.range) {
            -2 -> Bukkit.getOnlinePlayers().toList()
            -1 -> player.world.players
            else -> {
                if (channel.range > 0) {
                    nearbyPlayers(player, player.world.players, channel.range)
                } else emptyList()
            }
        }

        val viewers = candidatePlayers.filter { potentialViewer ->
            if (potentialViewer == player) return@filter true

            if (channel.usePermission && !potentialViewer.hasPermission("echat.${channel.name}.view")) {
                return@filter false
            }

            val viewerIsVanished = isPlayerVanished(potentialViewer)

            viewerIsVanished || !senderIsVanished
        }

        if (!senderIsVanished) {
            val visibleViewers = viewers.filter { v -> !isPlayerVanished(v) }
            if (visibleViewers.size <= 1) {
                EnderaOpenChat.scope.launch(getRegionDispatcher(player.location)) {
                    player.sendActionBar(config.messages.localnoone.cparse())
                }
            }
        }

        if (viewers.none { it != event.player }) {
            if (!senderIsVanished) {
                EnderaOpenChat.scope.launch(getRegionDispatcher(player.location)) {
                    player.sendActionBar(config.messages.localnoone.cparse())
                }
            }
        }

        val eViewers = event.viewers()

        eViewers.clear()
        eViewers.addAll(viewers)
        eViewers.add(Bukkit.getConsoleSender())

        event.renderer { _, _, message, _ ->
            message
        }

        event.message(
            channel.format
                .replace("{message}", stringMessage)
                .replace("{player}", player.name)
                .papiParse(player)
                .stringToComponent()
        )
    }

    fun getRegionDispatcher(location: Location): BukkitRegionDispatcher {
        return BukkitRegionDispatcher(EnderaOpenChat.instance, location)
    }

}