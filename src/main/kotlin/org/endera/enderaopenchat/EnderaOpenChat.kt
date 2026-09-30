package org.endera.enderaopenchat

import github.scarsz.discordsrv.DiscordSRV
import org.bukkit.plugin.java.JavaPlugin
import org.endera.enderalib.bstats.MetricsLite
import org.endera.enderalib.utils.PluginException
import org.endera.enderalib.utils.configuration.ConfigurationManager
import org.endera.enderaopenchat.commands.MsgCommand
import org.endera.enderaopenchat.commands.ReloadCommand
import org.endera.enderaopenchat.config.ConfigScheme
import org.endera.enderaopenchat.config.defaultConfig
import org.endera.enderaopenchat.discordsrv.DiscordSRVListener
import org.endera.enderaopenchat.listeners.ChatListener
import org.endera.enderaopenchat.listeners.LeaveJoinDeathListener
import java.io.File


class EnderaOpenChat : JavaPlugin() {

    companion object {
        lateinit var instance: EnderaOpenChat
        @Volatile
        lateinit var config: ConfigScheme
        lateinit var configurationManager: ConfigurationManager<ConfigScheme>
        var integrations: Set<Integrations> = emptySet()
    }

    override fun onEnable() {
        instance = this

        MetricsLite(this, 24253)

        configurationManager = ConfigurationManager(
            configFile = File(dataFolder, "config.yml"),
            dataFolder = dataFolder,
            defaultConfig = defaultConfig,
            logger = logger,
            serializer = ConfigScheme.serializer(),
            clazz = ConfigScheme::class
        )

        try {
            Companion.config = configurationManager.loadOrCreateConfig()
        } catch (e: PluginException) {
            logger.severe("Critical error loading configuration: ${e.message}")
            server.pluginManager.disablePlugin(this)
            return
        }

        val pm = server.pluginManager

        integrations = Integrations.entries.filterTo(mutableSetOf()) { pm.getPlugin(it.pluginName) != null }
        logger.info("Enabled integrations: ${integrations.joinToString { it.pluginName }.ifEmpty { "none" }}")

        if (Integrations.DISCORD_SRV in integrations) {
            DiscordSRV.api.subscribe(DiscordSRVListener)
        }

        pm.registerEvents(ChatListener(), this)
        pm.registerEvents(LeaveJoinDeathListener(), this)

        getCommand("msg")?.setExecutor(MsgCommand())
        getCommand("enderachat")?.setExecutor(ReloadCommand())
    }

    override fun onDisable() {
        if (Integrations.DISCORD_SRV in integrations) {
            DiscordSRV.api.unsubscribe(DiscordSRVListener)
        }
    }
}
