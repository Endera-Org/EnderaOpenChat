package org.endera.enderaopenchat.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.endera.enderalib.utils.checkPermission
import org.endera.enderalib.utils.configuration.ConfigurationManager
import org.endera.enderalib.utils.configuration.PluginException
import org.endera.enderaopenchat.config.ConfigScheme
import org.endera.enderaopenchat.config.config
import org.endera.enderaopenchat.config.configFile
import org.endera.enderaopenchat.config.defaultConfig
import org.endera.enderaopenchat.plugin
import org.endera.enderaopenchat.rlogger
import org.endera.enderaopenchat.utils.cparse

class ReloadCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.size != 1) return true

        sender.checkPermission("echat.reload") {
            val configManager = ConfigurationManager(
                configFile = configFile,
                dataFolder = plugin.dataFolder,
                defaultConfig = defaultConfig,
                logger = rlogger,
                serializer = ConfigScheme.serializer(),
                clazz = ConfigScheme::class
            )

            try {
                 config = configManager.loadOrCreateConfig()
                sender.sendMessage(config.messages.reload.cparse())
            } catch (e: PluginException) {
                rlogger.severe("Critical error loading configuration: ${e.message}")
                plugin.server.pluginManager.disablePlugin(plugin)
            }
        }

        return true
    }
}