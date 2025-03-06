package org.endera.enderaopenchat.discordsrv

import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.DiscordReadyEvent
import github.scarsz.discordsrv.api.events.GameChatMessagePostProcessEvent
import github.scarsz.discordsrv.util.DiscordUtil
import org.bukkit.plugin.Plugin
import org.endera.enderaopenchat.config.config

class DiscordSRVListener(private val plugin: Plugin) {

    @Subscribe
    fun discordReadyEvent(event: DiscordReadyEvent) {
        DiscordUtil.getJda().addEventListener(JDAListener(plugin))
        plugin.logger.info("Chatting on Discord with ${DiscordUtil.getJda().users.size} users!")
    }

    @Subscribe
    fun gameChatMessagePostProcessEvent(event: GameChatMessagePostProcessEvent) {
        val processedMessage = event.processedMessage
        val startsWithPrefix = processedMessage.startsWith(config.globalChat.prefix)

        when {
            config.discordSrv.sendMessagesFromLocalChat && !startsWithPrefix -> return
            !config.discordSrv.sendMessagesFromLocalChat && !startsWithPrefix -> {
                event.isCancelled = true
                return
            }
            else -> event.processedMessage = processedMessage.substring(config.globalChat.prefix.length)
        }
    }

//    @Subscribe(priority = ListenerPriority.MONITOR)
//    fun discordMessageReceived(event: DiscordGuildMessageReceivedEvent) {
//        plugin.logger.info("Received a chat message on Discord: ${event.message}")
//    }
//
//    @Subscribe(priority = ListenerPriority.MONITOR)
//    fun aMessageWasSentInADiscordGuildByTheBot(event: DiscordGuildMessageSentEvent) {
//        plugin.logger.info("A message was sent to Discord: ${event.message}")
//    }
//
//    @Subscribe
//    fun accountsLinked(event: AccountLinkedEvent) {
//        Bukkit.broadcastMessage("${event.player.name} just linked their MC account to their Discord user ${event.user}!")
//    }
//
//    @Subscribe
//    fun accountUnlinked(event: AccountUnlinkedEvent) {
//        val user = DiscordUtil.getJda().getUserById(event.discordId)
//        user?.openPrivateChannel()?.queue { privateChannel ->
//            privateChannel.sendMessage("Your account has been unlinked").queue()
//        }
//
//        val textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("unlinks")
//        if (textChannel != null) {
//            textChannel.sendMessage("${event.player.name} (${event.player.uniqueId}) has unlinked their associated Discord account: " +
//                    "${event.discordUser?.name ?: "<not available>"} (${event.discordId})").queue()
//        } else {
//            plugin.logger.warning("Channel called \"unlinks\" could not be found in the DiscordSRV configuration")
//        }
//    }
//
//    @Subscribe
//    fun discordMessageProcessed(event: DiscordGuildMessagePostProcessEvent) {
//        event.processedMessage = event.processedMessage.replace("cat", "dog")
//    }
}