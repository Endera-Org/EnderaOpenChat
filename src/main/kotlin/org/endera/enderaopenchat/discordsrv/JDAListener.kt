import github.scarsz.discordsrv.dependencies.jda.api.events.guild.GuildUnavailableEvent
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter
import org.bukkit.plugin.Plugin

class JDAListener(private val plugin: Plugin) : ListenerAdapter() {

    override fun onGuildUnavailable(event: GuildUnavailableEvent) {
        plugin.logger.severe("Oh no ${event.guild.name} went unavailable :(")
    }
}