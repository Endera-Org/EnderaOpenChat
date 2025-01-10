package org.endera.enderaopenchat

import discordsrv.DiscordSRVListener
import github.scarsz.discordsrv.DiscordSRV
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.endera.enderalib.utils.async.BukkitDispatcher
import org.endera.enderalib.utils.configuration.PluginException
import org.endera.enderalib.utils.configuration.configLoadCreationHandler
import org.endera.enderaopenchat.bstats.MetricsLite
import org.endera.enderaopenchat.commands.MsgCommand
import org.endera.enderaopenchat.commands.ReloadCommand
import org.endera.enderaopenchat.config.ConfigScheme
import org.endera.enderaopenchat.config.configFile
import org.endera.enderaopenchat.config.defaultConfig
import org.endera.enderaopenchat.listeners.ChatListener
import org.endera.enderaopenchat.listeners.LeaveJoinDeathListener
import java.io.File
import java.util.logging.Logger

lateinit var rlogger: Logger
lateinit var plugin: JavaPlugin
lateinit var bukkitDispatcher: BukkitDispatcher

class EnderaOpenChat : JavaPlugin() {

    val discordsrvListener = DiscordSRVListener(this)
    var discordSRV: Plugin? = null


    override fun onEnable() {
        plugin = this
        bukkitDispatcher = BukkitDispatcher(this)
        configFile = File("${dataFolder}/config.yml")
        rlogger = logger

        try {
            val loadedConfig = configLoadCreationHandler(
                configFile = configFile,
                dataFolder = dataFolder,
                defaultConfig = defaultConfig,
                logger = logger,
                serializer = ConfigScheme.serializer()
            )
            org.endera.enderaopenchat.config.config = loadedConfig
        } catch (e: PluginException) {
            logger.severe("Critical error loading configuration: ${e.message}")
            server.pluginManager.disablePlugin(this)
        }

        val metrics = MetricsLite(this, 24253)

        val discordSRV = Bukkit.getPluginManager().getPlugin("DiscordSRV")

        if (discordSRV != null) {
            DiscordSRV.api.subscribe(discordsrvListener)
        } else {
            logger.warning("DiscordSRV is not installed, skipping initialization.")
        }

        val pm = Bukkit.getPluginManager()
        pm.registerEvents(ChatListener(), this)
        pm.registerEvents(LeaveJoinDeathListener(), this)

        getCommand("msg")?.setExecutor(MsgCommand())
        getCommand("enderachat")?.setExecutor(ReloadCommand())
    }

    override fun onDisable() {
        if (discordSRV != null) {
            DiscordSRV.api.unsubscribe(discordsrvListener)
        } else {
            logger.severe("DiscordSRV is not installed, skipping initialization.")
        }
    }
}