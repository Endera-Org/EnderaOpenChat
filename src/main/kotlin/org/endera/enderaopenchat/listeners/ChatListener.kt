package org.endera.enderaopenchat.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
import org.endera.enderaopenchat.utils.papiParse


class ChatListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerChatSent(event: AsyncChatEvent) {
        val config = EnderaOpenChat.config
        val (nonPrefixedChannels, prefixedChannels) = EnderaOpenChat.config.channels.partition { it.prefix.isEmpty() }
        val stringMessage = event.message().componentToString()
        val containsPrefix = prefixedChannels.any { stringMessage.startsWith(it.prefix) }

        runBlocking {
            if (containsPrefix) {
                prefixedChannels.forEach { channel ->
                    if (!stringMessage.startsWith(channel.prefix)) return@runBlocking
                    val stringMessage2 = stringMessage.substring(channel.prefix.length)
                    processMessage(event, channel, stringMessage2)
                }
            } else {
                nonPrefixedChannels.forEach { channel ->
                    if (!stringMessage.startsWith(channel.prefix)) return@runBlocking
                    processMessage(event, channel, stringMessage)
                }
            }
        }

    }

    suspend fun processMessage(
        event: AsyncChatEvent,
        channel: ChatChannel,
        stringMessage: String,
    ) {
        val config = EnderaOpenChat.config
        val player = event.player

        if (!player.hasPermission("echat.${channel.name}.send") && channel.usePermission) {
            player.sendMessage(config.messages.nochannelpermission.cparse())
            event.isCancelled = true
            return
        }

        val nearbyPlayers = when (channel.range) {
            -2 -> {
                Bukkit.getOnlinePlayers()
            }
            -1 -> {
                withContext(getRegionDispatcher(event.player.location)) {
                    player.world.players
                }
            }
            else -> {
                if (channel.range > 0) {
                    withContext(getRegionDispatcher(event.player.location)) {
                        player.location.getNearbyPlayers(channel.range.toDouble())
                    }
                } else listOf()
            }
        }

        val viewers = if (channel.usePermission) {
                nearbyPlayers.filter { it.hasPermission("echat.${channel.name}.view") }
            } else {
                nearbyPlayers
            }


        if (viewers.isEmpty()) {
            withContext(getRegionDispatcher(player.location)) {
                player.sendActionBar(config.messages.localnoone.cparse())
            }
        }

        event.viewers().clear()
        event.viewers().addAll(viewers)
        event.viewers().add(Bukkit.getConsoleSender())

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