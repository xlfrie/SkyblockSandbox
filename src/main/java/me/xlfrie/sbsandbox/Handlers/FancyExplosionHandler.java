package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Illusioner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class FancyExplosionHandler implements Listener {
    public FancyExplosionHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void explosion(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof Illusioner)) return;
        Location loc = event.getLocation();
        for (Block block : event.blockList()) {
            if (Math.random() >= 0.05) continue;
            Vector vec = block.getLocation().toVector().subtract(loc.toVector()).multiply(Math.random() / 2);
            vec.setY(Math.random() * 2);
            FallingBlock fb = loc.getWorld().spawnFallingBlock(block.getLocation().add(0, 1, 0), block.getBlockData());
            fb.setVelocity(vec);
            fb.setDropItem(false);
        }
    }
}
