package me.xlfrie.sbsandbox.Events;

import io.netty.channel.ChannelHandlerContext;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEvent extends Event implements Cancellable {
    private final PacketEvent.direction direction;
    protected Object packet;
    private static HandlerList handlers = new HandlerList();
    private ChannelHandlerContext ctx;
    private boolean cancel;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public PacketEvent(Object packet, PacketEvent.direction direction, ChannelHandlerContext ctx) {
        this.packet = packet;
        this.direction = direction;
        this.ctx = ctx;
    }

    public Object getPacket() {
        return packet;
    }
    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public PacketEvent.direction getDirection() {
        return direction;
    }

    public enum direction {
        INBOUND,
        OUTBOUND
    }
}
