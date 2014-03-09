SNAdmin
=======

SNAdmin is a Bukkit administration plugin that provides many unique features that other admin plugins do not (such as Warnings and Muting). SNAdmin also uses SQL databases to store information, making it very fast and lightweight as well.

###Commands
`{args}` = Required, `[args]` = Optional

| Command                                                            | Permission                         |
| ------------------------------------------------------------------ |:----------------------------------:|
| /sna reload                                                        | skaianet.admin.reload              |
| /sna version                                                       | skaianet.admin.version             |
| /ban `{player}` `[reason]`                                         | skaianet.admin.ban                 |
| /tempban `{player}` `{time}` `{y/m/d/h/min/s}` `[reason]`          | skaianet.admin.ban.temp            |
| /ipban `{player}` `[reason]`                                       | skaianet.admin.ban.ip              |
| /unban `{player}`                                                  | skaianet.admin.unban[.ip]          |
| /mute `{player}` `[reason]`                                        | skaianet.admin.mute                |
| /tempmute `{player}` `{time}` `{y/m/d/h/min/s}` `[reason]`         | skaianet.admin.mute.temp           |
| /unmute `{player}`                                                 | skaianet.admin.unmute              |
| /warn `{player}` `[reason]`                                        | skaianet.admin.warn                |
| /clearwarns `{player/*}`                                           | skaianet.admin.warn.clear[.all]    |
| /kick `{player/*}` `[reason]`                                      | skaianet.admin.kick[.all]          |

###Permissions
The information provided here will go over permissions that were not listed above. Additional information is provided in the plugin.yml file found within the JAR.

| Permission                                              | Description                                           |
| ------------------------------------------------------- |:-----------------------------------------------------:|
| skaianet.admin.*                                        | Gives full access to all SNAdmin commands             |
| skaianet.admin.ban.*                                    | Gives full access to all ban-related commands         |
| skaianet.admin.kick.*                                   | Gives full access to all kick-related commands        |
| skaianet.admin.mute.*                                   | Gives full access to all mute-related commands        |
| skaianet.admin.warn.*                                   | Gives full access to all warn-related commands        |
| skaianet.admin.override.[*/ban/ipban/kick/mute]         | Allows a user to over ride bans, mutes, and kicks     |
