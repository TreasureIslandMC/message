package com.github.sarhatabaot.message;

import co.aikar.commands.bungee.contexts.OnlinePlayer;

import java.util.Objects;

/**
 * @author sarhatabaot
 */
public class MessagePlayer {
    private OnlinePlayer lastReceiver;

    public MessagePlayer(final OnlinePlayer lastReceiver) {
        this.lastReceiver = lastReceiver;
    }

    public OnlinePlayer getLastReceiver() {
        return lastReceiver;
    }

    public OnlinePlayer getLastPlayer() throws PlayerNotOnlineException {
        if(!getLastReceiver().getPlayer().isConnected()){
            throw new PlayerNotOnlineException(getLastPlayer()," is not online");
        }
        return getLastReceiver();
    }

    public static class PlayerNotOnlineException extends Exception {
        public PlayerNotOnlineException(final OnlinePlayer player, final String message) {
            super(player.getPlayer().getName()+message);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MessagePlayer that = (MessagePlayer) o;
        return lastReceiver.equals(that.lastReceiver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastReceiver);
    }
}
