package org.endera.enderaopenchat.config

val defaultConfig = ConfigScheme(
    channels = listOf(
        ChatChannel(
            name = "local",
            prefix = "",
            range = 100,
            sendToDiscord = false,
            usePermission = false,
            format = "<green>[L] <reset>{player}: {message}",
        ),
        ChatChannel(
            name = "global",
            prefix = "!",
            range = -2,
            sendToDiscord = true,
            usePermission = false,
            format = "<red>[G] <reset>{player}: {message}"

        ),
    ),
    personalMessages = Msg(
        format = "<gray>[<dark_gray>{sender} <gray>-> <dark_gray>{target}<gray>]: <white>{message}",
        sound = "entity.player.levelup",
        volume = 1f,
        pitch = 1f,
    ),
    customLeaveJoinDeath = CustomLeaveJoinDeath(
        joinMessage = LeaveJoinDeathMessage(
            enabled = true,
            message = "<gray>[<green>+<gray>] <white>{player} joined the game"
        ),
        leaveMessage = LeaveJoinDeathMessage(
            enabled = true,
            message = "<gray>[<red>-<gray>] <white>{player} left the game"
        ),
        deathMessage = LeaveJoinDeathMessage(
            enabled = true,
            message = "<gray>[<red>☠<gray>] <white>{player} has died"
        ),
    ),
    messages = Messages(
        prefix = "<green>[EChat]<reset>",
        reload = "{prefix} Plugin configuration reloaded",
        playernotfound = "{prefix} <red>Player not found",
        nochannelpermission = "{prefix} <red>You do not have permission to use this channel",
        localnoone = "<red>No players nearby!",
        usage = Usage(
            msg = "{prefix} <red>Command usage: /msg (player) (message)"
        )
    )
)
