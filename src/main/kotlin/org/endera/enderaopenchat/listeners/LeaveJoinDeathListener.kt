package org.endera.enderaopenchat.listeners

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.endera.enderalib.adventure.stringToComponent
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.config.LeaveJoinDeathMessage
import org.endera.enderaopenchat.utils.papiParse

class LeaveJoinDeathListener : Listener {

    private val messages get() = EnderaOpenChat.config.customLeaveJoinDeath

    private fun LeaveJoinDeathMessage.render(player: Player): Component? = message
        .takeIf { it.isNotBlank() }
        ?.replace("{player}", player.name)
        ?.papiParse(player)
        ?.stringToComponent()

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val message = messages.joinMessage
        if (message.enabled) event.joinMessage(message.render(event.player))
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val message = messages.leaveMessage
        if (message.enabled) event.quitMessage(message.render(event.player))
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val message = messages.deathMessage
        if (message.enabled) event.deathMessage(message.render(event.player))
    }
}
