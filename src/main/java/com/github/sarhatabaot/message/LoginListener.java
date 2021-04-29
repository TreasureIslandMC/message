package com.github.sarhatabaot.message;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author sarhatabaot
 */
public class LoginListener implements Listener {
    private MessagePlugin plugin;

    public LoginListener(final MessagePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerConnect(final PostLoginEvent event) {
        if(event.getPlayer().isConnected()) {
            plugin.getToggleCache().put(event.getPlayer().getUniqueId(), false);
            plugin.getPlayerCache().put(event.getPlayer().getUniqueId(), null);
        }
    }
}
