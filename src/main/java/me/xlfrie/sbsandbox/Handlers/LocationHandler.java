package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.Rank;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LocationHandler implements Listener {
    public LocationHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Rank.NONE.loadRank(player);
        player.setAllowFlight(true);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerGamePacketListenerImpl ps = craftPlayer.getHandle().connection;
        Location bedLoc = new Location(player.getWorld(), -0.5, 0, 4);
        bedLoc.setYaw(180);
        player.setBedSpawnLocation(bedLoc, true);
        player.teleport(bedLoc);
    }

    @EventHandler
    public void changeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        player.setAllowFlight(true);
        Location bedLoc = new Location(player.getWorld(), -0.5, 0, 4);
        bedLoc.setYaw(180);
        player.setBedSpawnLocation(bedLoc, true);
        player.teleport(bedLoc);
    }

    @EventHandler
    public void quitWorld(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Location bedLoc = new Location(Bukkit.getWorld("world"), -0.5, 0, 4);
        bedLoc.setYaw(180);
        player.setBedSpawnLocation(bedLoc, true);
        player.teleport(bedLoc);
    }
}
