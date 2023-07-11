package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.SBSandbox;
import me.xlfrie.sbsandbox.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PacketHandler implements Listener {
    public PacketHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        try {
            Utils.injectPacket(event.getPlayer());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        try {
            Utils.uninjectPacket(event.getPlayer());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
