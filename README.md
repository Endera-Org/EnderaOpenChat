# EnderaOpenChat

Simple and stable chat plugin for Paper and Folia. Requires [EnderaLib](https://github.com/Endera-Org/EnderaLib).
Optional integrations: PlaceholderAPI, DiscordSRV, CMI (vanish).

## Channels

A message goes to the channel with the longest matching `prefix`, otherwise to the channel with an empty prefix.
Formats use MiniMessage, `{player}`, `{message}` and PlaceholderAPI placeholders.

| `range` | Who sees the message |
|---------|----------------------|
| `-2`    | Everyone on the server |
| `-1`    | Everyone in the sender's world |
| `> 0`   | Players within that many blocks |

`send-to-discord` relays the channel to the DiscordSRV channel of the same name.

## Commands and permissions

| Command | Permission | Default |
|---------|------------|---------|
| `/msg <player> <message>` | `echat.msg` | everyone |
| `/echat reload` | `echat.reload` | op |

Channels with `use-permission: true` require `echat.<channel>.send` to write and `echat.<channel>.view` to read.

## Building

```
./gradlew build
```
