package org.endera.enderaopenchat.integrations

import com.Zrips.CMI.Containers.CMIUser
import org.bukkit.entity.Player

object CMIVanishHook {
    fun isPlayerVanished(player: Player): Boolean {
        val cmiUser = CMIUser.getUser(player)
        return cmiUser.isVanished
    }
}