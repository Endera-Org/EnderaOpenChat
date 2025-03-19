package org.endera.enderaopenchat.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigScheme(
    val channels: List<ChatChannel>,
    val personalMessages: Msg,
    val customLeaveJoinDeath: CustomLeaveJoinDeath,
    val messages: Messages,
)

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
    val sound: String,
    val volume: Float,
    val pitch: Float,
)

@Serializable
data class Messages(
    val prefix: String,
    val reload: String,
    val usage: Usage,
    val nochannelpermission: String,
    val localnoone: String,
    val playernotfound: String,
)


@Serializable
data class Usage(
    val msg: String
)