package org.endera.enderaopenchat.utils

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import org.endera.enderaopenchat.EnderaOpenChat
import org.endera.enderaopenchat.Integrations

fun String.papiParse(player: Player): String {
    if (EnderaOpenChat.integrations[Integrations.PLACEHOLDERAPI] == null) {
        return this
    }

    var text = this
    var previousText: String
    val maxRecursionDepth = 5
    var currentDepth = 0

    do {
        previousText = text
        text = PlaceholderAPI.setPlaceholders(player, text)
        currentDepth++
    } while (previousText != text && currentDepth < maxRecursionDepth)

    return text
}