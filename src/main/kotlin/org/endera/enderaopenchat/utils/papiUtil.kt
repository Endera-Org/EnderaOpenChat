package org.endera.enderaopenchat.utils

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.Integrations

fun String.papiParse(player: Player): String {
    return if (EnderaOpenChat.integrations[Integrations.PLACEHOLDERAPI] != null) {
        PlaceholderAPI.setPlaceholders(player, this)
    } else {
        this
    }
}