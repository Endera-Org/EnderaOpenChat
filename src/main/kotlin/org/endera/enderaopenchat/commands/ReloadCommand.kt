package org.endera.enderaopenchat.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.endera.enderalib.utils.PluginException
import org.endera.enderalib.utils.checkPermission
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.utils.cparse

class ReloadCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        sender.checkPermission("echat.reload") {
            if (args.singleOrNull() != "reload") {
                sender.sendMessage(EnderaOpenChat.config.messages.usage.echat.cparse())
                return@checkPermission
            }

            try {
                EnderaOpenChat.config = EnderaOpenChat.configurationManager.loadOrCreateConfig()
                sender.sendMessage(EnderaOpenChat.config.messages.reload.cparse())
            } catch (e: PluginException) {
                EnderaOpenChat.instance.logger.severe("Failed to reload configuration, keeping the previous one: ${e.message}")
                sender.sendMessage(EnderaOpenChat.config.messages.reloadfailed.cparse())
            }
        }

        return true
    }
}
