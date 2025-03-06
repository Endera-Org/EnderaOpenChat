package org.endera.enderaopenchat.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.endera.enderalib.adventure.stringToComponent
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.utils.papiParse

class LeaveJoinDeathListener : Listener {

    val config = EnderaOpenChat.config

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!config.customLeaveJoinDeath.joinMessage.enabled) return
        if (config.customLeaveJoinDeath.joinMessage.message.isBlank()) {
            event.joinMessage(null)
            return
        }
        event.joinMessage(
            config.customLeaveJoinDeath.joinMessage.message
                .replace("{player}", event.player.name)
                .papiParse(event.player)
                .stringToComponent()
        )

    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (!config.customLeaveJoinDeath.leaveMessage.enabled) return
        if (config.customLeaveJoinDeath.leaveMessage.message.isBlank()) {
            event.quitMessage(null)
            return
        }
        event.quitMessage(
            config.customLeaveJoinDeath.leaveMessage.message
                .replace("{player}", event.player.name)
                .papiParse(event.player)
                .stringToComponent()
        )
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (!config.customLeaveJoinDeath.deathMessage.enabled) return
        if (config.customLeaveJoinDeath.deathMessage.message.isBlank()) {
            event.deathMessage(null)
            return
        }
        event.deathMessage(
            config.customLeaveJoinDeath.deathMessage.message
                .replace("{player}", event.player.name)
                .papiParse(event.player)
                .stringToComponent()
        )
    }
}