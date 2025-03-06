package org.endera.enderaopenchat.utils

import net.kyori.adventure.text.Component
import org.endera.enderalib.adventure.stringToComponent
import org.endera.enderaopenchat.EnderaOpenChat

fun String.cparse() : Component {
    return this.replace("{prefix}", EnderaOpenChat.config.messages.prefix).stringToComponent()
}