package org.endera.enderaopenchat.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.endera.enderalib.adventure.componentToString
import org.endera.enderalib.adventure.stringToComponent
import org.endera.enderalib.isFolia
import org.endera.enderalib.utils.async.BukkitRegionDispatcher
import org.endera.enderaopenchat.bukkitDispatcher
import org.endera.enderaopenchat.config.config
import org.endera.enderaopenchat.plugin
import org.endera.enderaopenchat.utils.cparse


class ChatListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerChatSent(event: AsyncChatEvent) {

        val bukkitRegionDispatcher = BukkitRegionDispatcher(plugin, event.player.location)

        runBlocking {
            val player = event.player
            val stringMessage = event.message().componentToString()

            if (stringMessage.startsWith(config.globalChat.prefix)) {
//            val renderer = EChatRenderer()

                event.renderer { _, _, message, _ -> message }
                event.message(
                    PlaceholderAPI.setPlaceholders(
                        event.player,
                        config.globalChat.format
                            .replace("{message}", stringMessage.substring(config.globalChat.prefix.length))
                            .replace("{player}", player.name)
                    ).stringToComponent()
                )
                return@runBlocking
            }

            // LOCAL CHAT HERE
            val nearbyPlayers = if(isFolia) {
                withContext(bukkitRegionDispatcher) {
                    player.location.getNearbyPlayers(config.localChat.range.toDouble())
                }
            } else {
                withContext(bukkitDispatcher) {
                    player.location.getNearbyPlayers(config.localChat.range.toDouble())
                }
            }

            if (config.messages.localnoone.isNotEmpty() && nearbyPlayers.size == 1) {
                player.sendActionBar(config.messages.localnoone.cparse())
            }

            event.viewers().clear()
            event.viewers().addAll(nearbyPlayers)
            event.viewers().add(Bukkit.getConsoleSender())

            event.renderer { _, _, message, _ ->
                message
            }

            event.message(
                PlaceholderAPI.setPlaceholders(
                    event.player,
                    config.localChat.format
                        .replace("{message}", stringMessage)
                        .replace("{player}", player.name)
                ).stringToComponent()
            )
        }

    }

}