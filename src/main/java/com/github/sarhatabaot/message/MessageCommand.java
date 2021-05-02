package com.github.sarhatabaot.message;

import co.aikar.commands.ACFBungeeUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.apachecommonslang.ApacheCommonsLangUtil;
import co.aikar.commands.bungee.contexts.OnlinePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author sarhatabaot
 */
public class MessageCommand extends BaseCommand {
    private final MessagePlugin plugin;
    private Configuration configuration;

    public MessageCommand(final MessagePlugin plugin) {
        this.plugin = plugin;
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException exception) {
            plugin.getLogger().severe("Could not load config.yml, disabling plugin.");
            plugin.onDisable();
        }

    }

    @Default
    @CommandAlias("msg")
    @CommandPermission("message.message")
    @CommandCompletion("@players")
    public void onMessage(final ProxiedPlayer player, final OnlinePlayer receiver, final String message) {
        if (plugin.isToggled(receiver.getPlayer()) && !player.hasPermission("message.toggle.bypass")) {
            //this player has toggles off their messages
            player.sendMessage(receiver.getPlayer().getName() + " has messages toggled off.");
            return;
        }

        if (player.equals(receiver.getPlayer())) {
            player.sendMessage("You can't send a message to yourself.");
            return;
        }

        //otherwise, send a message
        player.sendMessage(formatMessage(player, receiver, configuration.getString("message.send"), message));
        receiver.getPlayer().sendMessage(formatMessage(player, receiver, configuration.getString("message.receive"), message));
        plugin.getLogger().info(player.getName() + " -> " + receiver.getPlayer().getName() + " : " + message);

        cacheLastPlayer(player.getUniqueId(), new MessagePlayer(receiver));
    }


    private void cacheLastPlayer(final UUID uuid, final MessagePlayer messagePlayer) {
        if (!plugin.getPlayerCache().containsKey(uuid))
            plugin.getPlayerCache().put(uuid, messagePlayer);
        if (!plugin.getPlayerCache().get(uuid).equals(messagePlayer))
            plugin.getPlayerCache().put(uuid, messagePlayer);
    }

    private String formatMessage(final ProxiedPlayer sender, final OnlinePlayer receiver, final String format, final String message) {
        return ChatColor.translateAlternateColorCodes('&', format.replace("%sender_name%", sender.getName())
                .replace("%sender_display_name%", sender.getDisplayName())
                .replace("%sender_server%", formatServerName(sender.getServer().getInfo().getName()))
                .replace("%receiver_name%", receiver.getPlayer().getName())
                .replace("%receiver_display_name%", receiver.getPlayer().getDisplayName())
                .replace("%receiver_server%", formatServerName(receiver.getPlayer().getServer().getInfo().getName())))
                .replace("%message%", message);
    }

    private String formatServerName(final String serverName) {
        return configuration.getString("servers." + serverName);
    }

    @CommandAlias("r|reply")
    @CommandPermission("message.reply")
    public void onReply(final ProxiedPlayer player, final String message) {
        try {
            final OnlinePlayer receiver = plugin.getPlayerCache().get(player.getUniqueId()).getLastPlayer();
            receiver.getPlayer().sendMessage(formatMessage(player, receiver, configuration.getString("message.receive"), message));
            player.sendMessage(formatMessage(player, receiver, configuration.getString("message.send"), message));
            plugin.getLogger().info(player.getName() + " -> " + receiver.getPlayer().getName() + " : " + message);
        } catch (MessagePlayer.PlayerNotOnlineException exception) {
            player.sendMessage("Player is not online anymore.");
        } catch (NullPointerException nullPointerException) {
            player.sendMessage("You don't have anyone to reply to.");
        }
    }

    @CommandAlias("message")
    public class PluginCommands extends BaseCommand {

        @Subcommand("reload")
        @CommandPermission("message.reload")
        public void onReload(final ProxiedPlayer player) {
            try {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
                player.sendMessage("Reloaded the configuration files.");
            } catch (IOException exception) {
                plugin.getLogger().severe("Could not load config.yml, disabling plugin.");
                plugin.getLogger().severe(exception.getMessage());
                player.sendMessage("Could not reload config.yml, check for yaml mistakes.");
            }
        }

        @HelpCommand
        public void onHelp(CommandHelp commandHelp) {
            commandHelp.showHelp();
        }

        @Subcommand("toggle")
        @CommandPermission("message.toggle")
        public void onToggle(final ProxiedPlayer player) {
            if (plugin.isToggled(player)) {
                plugin.getToggleCache().put(player.getUniqueId(), false);
                player.sendMessage("Toggled messages on.");
            }
            plugin.getToggleCache().put(player.getUniqueId(), true);
            player.sendMessage("Toggles messages off.");
        }

        @Subcommand("onlineplayers")
        public void onLogTest(final ProxiedPlayer sender,@Optional String input) {
            ACFBungeeUtil.validate(sender, "Sender cannot be null");

            ArrayList<String> matchedPlayers = new ArrayList<>();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                String name = player.getName();
                if (input == null) {
                    matchedPlayers.add(name);
                } else if(ApacheCommonsLangUtil.startsWithIgnoreCase(name, input)) {
                    matchedPlayers.add(name);
                }
            }

            matchedPlayers.sort(String.CASE_INSENSITIVE_ORDER);
            plugin.getLogger().info(matchedPlayers.toString());
        }
    }

    

}



