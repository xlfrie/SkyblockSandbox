package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import me.xlfrie.sbsandbox.Mobs.GyroBlock;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.Collection;
import java.util.HashMap;

public class GyrokineticWand2 extends CustomItemBase {
    private final HashMap<String, Object> _itemData = new HashMap<>() {{
        put("manaCost", 1200);
        put("abilityName", "Gravity Storm");
        put("health", 0);
        put("intelligence", 0);
    }};

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.LEFT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_AIR)) return;
        if (canUse(item, player)) return;

        new GyroTask(player).runTaskTimer(SBSandbox.getInstance(), 0, 4);

        if (action.equals(Action.LEFT_CLICK_BLOCK)) event.setCancelled(true);
    }

    @Override
    public String getId() {
        return "GYROKINETIC_WAND";
    }

    @Override
    public String localizedName() {
        return "§5Gyrokinetic Wand";
    }

    @Override
    public double cooldownDelay() {
        return 30;
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public HashMap<String, Object> itemData() {
        return _itemData;
    }

    private void rift(Location location, int radius) {
        HashMap<Tuple<Integer, Integer>, Double> locs = new HashMap<>();
        for (int x = 0; x < radius; x++) {
            double v = Math.sin(Math.acos((float) x / radius)) * radius;
            locs.put(new Tuple<>(x, (int) v), v);

            double w = Math.cos(Math.asin((float) x / radius)) * radius;
            locs.put(new Tuple<>((int) w, x), (double) x);
        }


        locs.forEach((tuple, val) -> mirrorGyroBlock(location, tuple.getA(), val));
    }

    private void mirrorBlock(Location location, int x, double v) {
        location.getWorld().setBlockData((int) location.getX() + x, (int) location.getY(), (int) (location.getZ() + v), Material.OBSIDIAN.createBlockData());
        location.getWorld().setBlockData((int) location.getX() + x, (int) location.getY(), (int) (location.getZ() - v), Material.OBSIDIAN.createBlockData());
        location.getWorld().setBlockData((int) location.getX() - x, (int) location.getY(), (int) (location.getZ() + v), Material.OBSIDIAN.createBlockData());
        location.getWorld().setBlockData((int) location.getX() - x, (int) location.getY(), (int) (location.getZ() - v), Material.OBSIDIAN.createBlockData());
    }

    public void mirrorGyroBlock(Location location, int x, double v) {
        new GyroBlock(EntityType.FALLING_BLOCK, ((CraftWorld) location.getWorld()).getHandle(), (int) location.getX() + x, (int) location.getY(), (int) (location.getZ() + v), location);
        new GyroBlock(EntityType.FALLING_BLOCK, ((CraftWorld) location.getWorld()).getHandle(), (int) location.getX() + x, (int) location.getY(), (int) (location.getZ() - v), location);
        new GyroBlock(EntityType.FALLING_BLOCK, ((CraftWorld) location.getWorld()).getHandle(), (int) location.getX() - x, (int) location.getY(), (int) (location.getZ() + v), location);
        new GyroBlock(EntityType.FALLING_BLOCK, ((CraftWorld) location.getWorld()).getHandle(), (int) location.getX() - x, (int) location.getY(), (int) (location.getZ() - v), location);
    }

    public class GyroTask extends BukkitRunnable {
        int i = 8;
        final Player player;
        final RayTraceResult ray;
        Location rift;
        Collection<Entity> pulling;

        public GyroTask(Player player) {
            this.player = player;
            this.ray = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 26);

            if (ray != null) {
                this.rift = ray.getHitBlock().getLocation().add(0.5, 0, 0.5);
                abilityRegister(player);
                this.pulling = rift.getWorld().getNearbyEntities(rift, 8, 8, 8);

            } else {
                i = 0;
                player.sendMessage("§cYou must aim at the floor.");
            }
        }

        @Override
        public void run() {
            if (i <= 0) this.cancel();

            for (float j = 0; j < Math.PI * 2; j += 0.2) {
                player.spawnParticle(Particle.SPELL_WITCH, rift.clone().add(Math.sin(j + (0.4 * i)) * i, i / 5F + 1, Math.cos(j + (0.4 * i)) * i), 1, 0, 0, 0, 0);
            }

            pulling.forEach(entity -> {
                if (entity.getType() != org.bukkit.entity.EntityType.ARMOR_STAND && entity.getType() != org.bukkit.entity.EntityType.PLAYER)
                    entity.setVelocity(rift.toVector().subtract(entity.getLocation().toVector()).multiply(0.35));
            });

            rift(rift, i);

            i--;
        }
    }
}
