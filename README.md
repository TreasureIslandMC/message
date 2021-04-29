
# Message

Simple /msg plugin for bungeecord.

## Commands & Permissions:
### Message
Send messages to players.
* `/msg <player> <message>  ` - `message.message`
### Reply
Reply to messages.
* `/reply <message>` or `/r <message>` - `message.reply`
### Toggle
Toggle being able to receive messages.
*`/message toggle` - `message.toggle`
### Reload
Reload the configuration file.
* `/message reload` - `message.reload`
### Help
Help menu, pretty bare-bones.
* `/message help`

## Config
```yaml
# Server name in _server placeholder.  
# Use the server names as they appear in bungee's config.yml.  
servers:  
  lobby: "&f&lLobby"  
  lobby2: "&b&lLobby2"  
  
# Supported placeholders  
# %message%  
# %receiver_display_name% %receiver_name% %receiver_server%  
# %sender_display_name% %sender_name% %sender_server%  
message:  
  send: "&7[%sender_server%&7] &6%sender_name% &7> &7[%receiver_server%&7] &6%receiver_name%&7: &2%message%"  
  receive: "&7[%sender_server%&7] &6%sender_name% &7> &7[%receiver_server%&7] &6%receiver_name%&7: &2%message%"
```
