package org.endera.enderaopenchat.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigScheme(
    val channels: List<ChatChannel>,
    val personalMessages: Msg,
    val customLeaveJoinDeath: CustomLeaveJoinDeath,
    val messages: Messages,
)

fun ConfigScheme.resolveChannel(message: String): Pair<ChatChannel, String>? {
    val prefixed = channels
        .filter { it.prefix.isNotEmpty() && message.startsWith(it.prefix) }
        .maxByOrNull { it.prefix.length }
    val remainder = prefixed?.let { message.removePrefix(it.prefix).trimStart() }

    if (prefixed != null && !remainder.isNullOrBlank()) return prefixed to remainder
    return channels.firstOrNull { it.prefix.isEmpty() }?.let { it to message }
}

@Serializable
data class ChatChannel(
    val name: String,
    val prefix: String,
    val sendToDiscord: Boolean,
    val usePermission: Boolean,
    val range: Int,
    val format: String
)

@Serializable
data class CustomLeaveJoinDeath(
    val joinMessage: LeaveJoinDeathMessage,
    val leaveMessage: LeaveJoinDeathMessage,
    val deathMessage: LeaveJoinDeathMessage,
)

@Serializable
data class LeaveJoinDeathMessage(
    val enabled: Boolean,
    val message: String,
)

@Serializable
data class Msg(
    val format: String,
    val self: String,
    val sound: String,
    val volume: Float,
    val pitch: Float,
)

@Serializable
data class Messages(
    val prefix: String,
    val reload: String,
    val reloadfailed: String,
    val usage: Usage,
    val nochannelpermission: String,
    val localnoone: String,
    val playernotfound: String,
)


@Serializable
data class Usage(
    val msg: String,
    val echat: String,
)