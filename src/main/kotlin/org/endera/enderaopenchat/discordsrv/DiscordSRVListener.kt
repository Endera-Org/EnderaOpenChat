package org.endera.enderaopenchat.discordsrv

import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent
import github.scarsz.discordsrv.util.MessageUtil
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.config.resolveChannel

@Suppress("unused")
object DiscordSRVListener {

    @Subscribe
    fun onGameChatPreProcess(event: GameChatMessagePreProcessEvent) {
        val plain = MessageUtil.strip(MessageUtil.toLegacy(event.messageComponent))
        val (channel, message) = EnderaOpenChat.config.resolveChannel(plain) ?: run {
            event.isCancelled = true
            return
        }

        val canSend = !channel.usePermission || event.player.hasPermission("echat.${channel.name}.send")
        if (!channel.sendToDiscord || !canSend) {
            event.isCancelled = true
            return
        }

        event.channel = channel.name
        if (channel.prefix.isNotEmpty()) {
            event.messageComponent = MessageUtil.toComponent(message, true)
        }
    }
}
