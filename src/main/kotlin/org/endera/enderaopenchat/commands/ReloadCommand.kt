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

        if (args.size != 1) return true

        sender.checkPermission("echat.reload") {
            try {
                EnderaOpenChat.config = EnderaOpenChat.configurationManager.loadOrCreateConfig()
                sender.sendMessage(EnderaOpenChat.config.messages.reload.cparse())
            } catch (e: PluginException) {
                EnderaOpenChat.instance.logger.severe("Critical error loading configuration: ${e.message}")
                EnderaOpenChat.instance.server.pluginManager.disablePlugin(EnderaOpenChat.instance)
            }
        }

        return true
    }
}