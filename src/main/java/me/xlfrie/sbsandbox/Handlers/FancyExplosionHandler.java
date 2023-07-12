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

import java.util.Random;

public class FancyExplosionHandler implements Listener {
    public FancyExplosionHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void explosion(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof Illusioner)) return;
        Location loc = event.getLocation();
        Random random = new Random();
        double chance = 100;
        for (int i = 0; i < (event.blockList().size() * (chance / 100)); i++) {
            Block block = event.blockList().get(random.nextInt(1, event.blockList().size()));
            Vector vec = block.getLocation().toVector().subtract(loc.toVector()).multiply(random.nextDouble(-0.5, 0.5));
            vec.multiply(random.nextDouble(0, 0.5));
            vec.setY(Math.random() * 2);
            FallingBlock fb = loc.getWorld().spawnFallingBlock(block.getLocation().add(0, 1, 0), block.getBlockData());
            fb.setVelocity(vec);
            fb.setDropItem(false);
        }
    }
}
