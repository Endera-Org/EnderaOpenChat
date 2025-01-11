package org.endera.enderaopenchat.config

val defaultConfig = ConfigScheme(
    localChat = LocalChat(
        range = 100,
        format = "<green>[L] <reset>%luckperms_prefix%{player}: {message}",
    ),
    globalChat = GlobalChat(
        prefix = "!",
        format = "<red>[G] <reset>%luckperms_prefix%{player}: {message}"
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
    discordSrv = DiscordSrv(
        sendMessagesFromLocalChat = false
    ),
    messages = Messages(
        prefix = "<green>[EChat]<reset>",
        reload = "{prefix} Plugin configuration reloaded",
        playernotfound = "{prefix} <red>Player not found",
        localnoone = "<red>No players nearby!",
        usage = Usage(
            msg = "{prefix} <red>Command usage: /msg (player) (message)"
        )
    )
)
