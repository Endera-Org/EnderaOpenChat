package org.endera.enderaopenchat

import github.scarsz.discordsrv.DiscordSRV
import kotlinx.coroutines.CoroutineScope
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.endera.enderalib.bstats.MetricsLite
import org.endera.enderalib.utils.async.BukkitDispatcher
import org.endera.enderalib.utils.async.ioDispatcher
import org.endera.enderalib.utils.configuration.ConfigurationManager
import org.endera.enderalib.utils.configuration.PluginException
import org.endera.enderaopenchat.commands.MsgCommand
import org.endera.enderaopenchat.commands.ReloadCommand
import org.endera.enderaopenchat.config.ConfigScheme
import org.endera.enderaopenchat.config.defaultConfig
import org.endera.enderaopenchat.discordsrv.DiscordSRVListener
import org.endera.enderaopenchat.listeners.ChatListener
import org.endera.enderaopenchat.listeners.LeaveJoinDeathListener
import java.io.File


@Suppress("unused")
class EnderaOpenChat : JavaPlugin() {

    companion object {
        lateinit var instance : EnderaOpenChat
        lateinit var bukkitDispatcher: BukkitDispatcher
        lateinit var configFile: File
        lateinit var config: ConfigScheme
        lateinit var integrations: Map<Integrations, Plugin?>
        val scope = CoroutineScope(ioDispatcher)
    }

    val discordsrvListener = DiscordSRVListener(this)


    override fun onEnable() {
        instance = this
        bukkitDispatcher = BukkitDispatcher(this)
        configFile = File("${dataFolder}/config.yml")

        val configManager = ConfigurationManager(
            configFile = configFile,
            dataFolder = dataFolder,
            defaultConfig = defaultConfig,
            logger = logger,
            serializer = ConfigScheme.serializer(),
            clazz = ConfigScheme::class
        )

        try {
            Companion.config = configManager.loadOrCreateConfig()
        } catch (e: PluginException) {
            logger.severe("Critical error loading configuration: ${e.message}")
            server.pluginManager.disablePlugin(this)
        }

        val metrics = MetricsLite(this, 24253)

        integrations = mapOf<Integrations, Plugin?>(
            Integrations.PLACEHOLDERAPI to Bukkit.getPluginManager().getPlugin("DiscordSRV"),
            Integrations.DISCORD_SRV to Bukkit.getPluginManager().getPlugin("DiscordSRV")
        )

        integrations.forEach { integration, plugin ->
            if (plugin == null) {
                logger.warning("${integration.pluginName} is not installed, skipping initialization.")
            }
        }

        if (integrations[Integrations.DISCORD_SRV] != null) {
            DiscordSRV.api.subscribe(discordsrvListener)
        }

        val pm = Bukkit.getPluginManager()
        pm.registerEvents(ChatListener(), this)
        pm.registerEvents(LeaveJoinDeathListener(), this)

        getCommand("msg")?.setExecutor(MsgCommand())
        getCommand("enderachat")?.setExecutor(ReloadCommand())
    }

    override fun onDisable() {
        if (integrations[Integrations.DISCORD_SRV] != null) {
            DiscordSRV.api.unsubscribe(discordsrvListener)
        }
    }
}