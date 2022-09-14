package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.Rank;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.WorldInitEvent;

import java.util.List;

public class LandProtection implements Listener {
    private final List<List<Integer>> protectedChunks = List.of(
            List.of(-1, 0),
            List.of(-1, -1),
            List.of(0, -1),
            List.of(0, 0)
    );

    public LandProtection(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void interact(BlockBreakEvent event) {
        if (Rank.NONE.getRank(event.getPlayer()).getHierarchy() >= 2) return;
        if (!protectedChunks.contains(List.of(event.getBlock().getChunk().getX(), event.getBlock().getChunk().getZ()))
                && !event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world"))
            return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(SBSandbox.color("&cBruh!&7 You cannot build here!"));
        CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer();
        Location blockLoc = event.getBlock().getLocation().add(0.5, 1.5, 0.5);
        craftPlayer.getHandle().connection.send(new ClientboundLevelParticlesPacket(ParticleTypes.SMOKE, false, blockLoc.getX(), blockLoc.getY(), blockLoc.getZ(), 0, (float) 0.25, 0, 0, 5));
    }

    @EventHandler
    public void update(BlockPlaceEvent event) {
        if (Rank.NONE.getRank(event.getPlayer()).getHierarchy() >= 2) return;
        if (!protectedChunks.contains(List.of(event.getBlock().getChunk().getX(), event.getBlock().getChunk().getZ()))
                && !event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world"))
            return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(SBSandbox.color("&cBruh!&7 You cannot build here!"));
        CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer();
        Location blockLoc = event.getBlock().getLocation().add(0.5, 1.5, 0.5);
        craftPlayer.getHandle().connection.send(new ClientboundLevelParticlesPacket(ParticleTypes.SMOKE, false, blockLoc.getX(), blockLoc.getY(), blockLoc.getZ(), 0, (float) 0.25, 0, 0, 5));
    }

    @EventHandler
    public void explode(EntityExplodeEvent event) {
//        if (event.getEntity().getType() == EntityType.PLAYER && Rank.NONE.getRank(((Player) event.getEntity())).getHierarchy() >= 2) return;
//        event.blockList().removeIf((b) -> protectedChunks.contains(List.of(b.getChunk().getX(), b.getChunk().getZ())) || b.getLocation().getWorld().getName().equalsIgnoreCase("world"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void worldInit(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false);
    }
}
