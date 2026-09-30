package org.endera.enderaopenchat.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.endera.enderalib.adventure.minimessage
import org.endera.enderalib.adventure.stringToComponent
import org.endera.enderalib.utils.checkPermission
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.utils.cparse
import org.endera.enderaopenchat.utils.isPlayerVanished

class MsgCommand : CommandExecutor {
    private val config get() = EnderaOpenChat.config

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        sender.checkPermission("echat.msg") {
            if (args.size < 2) {
                sender.sendMessage(config.messages.usage.msg.cparse())
                return@checkPermission
            }

            val targetPlayer = Bukkit.getPlayer(args[0])
            val hiddenFromSender = targetPlayer != null && sender is Player
                    && isPlayerVanished(targetPlayer) && !isPlayerVanished(sender)

            if (targetPlayer == null || !targetPlayer.isOnline || hiddenFromSender) {
                sender.sendMessage(config.messages.playernotfound.cparse())
                return@checkPermission
            }

            val personal = config.personalMessages
            val message = minimessage.escapeTags(args.drop(1).joinToString(" "))

            fun render(senderName: String, targetName: String) = personal.format
                .replace("{sender}", senderName)
                .replace("{target}", targetName)
                .replace("{message}", message)
                .stringToComponent()

            sender.sendMessage(render(personal.self, targetPlayer.name))
            targetPlayer.sendMessage(render(sender.name, personal.self))

            if (personal.sound.isNotBlank()) {
                targetPlayer.playSound(targetPlayer.location, personal.sound, personal.volume, personal.pitch)
            }
        }
        return true
    }
}
