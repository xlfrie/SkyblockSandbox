package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SoulWhip extends CustomItemBase {
    @Override
    public String getId() {
        return "SOULWHIP";
    }

    @Override
    public String localizedName() {
        return "§6Soul Whip";
    }

    @Override
    public double cooldownDelay() {
        return 0.2;
    }

    @Override
    public Material getMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public void abilityRegister(Player player) {
        if (!player.hasPermission("owner"))
            cooldowns.get(player.getUniqueId()).put(this.getId(), System.currentTimeMillis());
    }

    private HashMap<String, Object> _itemData = new HashMap<>() {{
        put("health", 0);
        put("intelligence", 10000000);
    }};

    @Override
    public HashMap<String, Object> itemData() {
        return _itemData;
    }

    @EventHandler
    public void cast(PlayerFishEvent event) {
        if (canUse(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer())) return;
        event.setCancelled(true);
        int height = 1;
        int dist = 15;
        Player player = event.getPlayer();
        Location p0 = player.getEyeLocation();
        Block block = player.getTargetBlockExact(dist * 10);
        Location p2 = block == null ? player.getLocation().add(player.getLocation().getDirection().multiply(4)) : player.getLocation().distance(block.getLocation()) > dist ? player.getLocation().clone().add(player.getLocation().getDirection().multiply(dist)) : p0.clone().add(player.getLocation().getDirection().multiply(player.getLocation().distance(block.getLocation())));
        Location p1 = player.getEyeLocation().add(0, height, 0).add(player.getLocation().getDirection().multiply(player.getLocation().distance(p2) / 2));
//        p2 = player.getEyeLocation().add(0, height, 0).add(player.getLocation().getDirection().multiply(player.getLocation().distance(p2)));
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 0.15F, 1.5F);

        abilityRegister(player);
        new BukkitRunnable() {
            int ticks = 0;
            List<UUID> hitBefore = new ArrayList<>();
            @Override
            public void run() {
                if (ticks >= 10) this.cancel();
                ticks += 1;
                for (int i = 0; i < 5; i++) {
                    Location damageLoc = bezierPoint(ticks / 10F, p0, p1, p2);
                    damageLoc.setY(p2.getY()+2);
                    for (Entity nearbyEntity : player.getWorld().getNearbyEntities(damageLoc, 1, 1, 1)) {
                        if (nearbyEntity instanceof Damageable && nearbyEntity.getUniqueId() != player.getUniqueId() && !hitBefore.contains(nearbyEntity.getUniqueId())) {
                            ((Damageable) nearbyEntity).damage(10, player);
                            hitBefore.add(nearbyEntity.getUniqueId());
                        }
                    }
                    player.getWorld().spawnParticle(Particle.REDSTONE, bezierPoint((float) ((ticks + Math.random()) / 10F), p0, p1, p2), 1, new Particle.DustOptions(Color.fromBGR(30, 30, 30), 1));
                    player.getWorld().spawnParticle(Particle.REDSTONE, bezierPoint((float) ((ticks + Math.random()) / 10F), p0, p1, p2), 1, new Particle.DustOptions(Color.fromBGR(30, 30, 90), 1));
                }
            }
        }.runTaskTimer(SBSandbox.getInstance(), 0L, 0L);
    }

    public static Location bezierPoint(float t, Location p0, Location p1, Location p2) {
        float a = (1 - t) * (1 - t);
        float b = 2 * (1 - t) * t;
        float c = t * t;

        Location p = p0.clone().multiply(a).add(p1.clone().multiply(b)).add(p2.clone().multiply(c));
        return p;
    }

}
