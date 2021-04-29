package com.github.sarhatabaot.message;

import co.aikar.commands.BungeeCommandManager;
import co.aikar.commands.bungee.contexts.OnlinePlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MessagePlugin extends Plugin {
    private Map<UUID, MessagePlayer> playerCache;
    private Map<UUID, Boolean> toggleCache;

    @Override
    public void onEnable() {
        saveDefaultConfiguration();
        BungeeCommandManager commandManager = new BungeeCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new MessageCommand(this));
        playerCache = new HashMap<>();
        toggleCache = new HashMap<>();

        // Plugin startup logic
    }

    private void saveDefaultConfiguration() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean isToggled(final ProxiedPlayer player) {
        if(!toggleCache.containsKey(player.getUniqueId()))
            toggleCache.put(player.getUniqueId(),false);
        return toggleCache.get(player.getUniqueId());
    }

    public Map<UUID, MessagePlayer> getPlayerCache() {
        return playerCache;
    }

    public Map<UUID, Boolean> getToggleCache() {
        return toggleCache;
    }
}
