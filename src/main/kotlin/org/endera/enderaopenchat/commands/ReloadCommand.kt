package org.endera.enderaopenchat.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.endera.enderalib.utils.checkPermission
import org.endera.enderalib.utils.configuration.ConfigurationManager
import org.endera.enderalib.utils.configuration.PluginException
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.config.ConfigScheme
import org.endera.enderaopenchat.config.defaultConfig
import org.endera.enderaopenchat.utils.cparse

class ReloadCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.size != 1) return true

        sender.checkPermission("echat.reload") {
            val configManager = ConfigurationManager(
                configFile = EnderaOpenChat.configFile,
                dataFolder = EnderaOpenChat.instance.dataFolder,
                defaultConfig = defaultConfig,
                logger = EnderaOpenChat.instance.logger,
                serializer = ConfigScheme.serializer(),
                clazz = ConfigScheme::class
            )

            try {
                EnderaOpenChat.config = configManager.loadOrCreateConfig()
                sender.sendMessage(EnderaOpenChat.config.messages.reload.cparse())
            } catch (e: PluginException) {
                EnderaOpenChat.instance.logger.severe("Critical error loading configuration: ${e.message}")
                EnderaOpenChat.instance.server.pluginManager.disablePlugin(EnderaOpenChat.instance)
            }
        }

        return true
    }
}