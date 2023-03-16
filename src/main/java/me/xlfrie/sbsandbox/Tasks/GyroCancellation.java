package me.xlfrie.sbsandbox.Tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class GyroCancellation extends BukkitRunnable {
    private int count;
    private final Player player;
    private final Vector rift;
    private final ArrayList<Location> usedLocs;
    private final Material[] blocks;

    public GyroCancellation(JavaPlugin plugin, int count, Player player, Vector rift) {
        this.player = player;
        this.rift = rift;
        this.usedLocs = new ArrayList<Location>();
        this.blocks = new Material[6];
        this.blocks[0] = Material.OBSIDIAN;
        this.blocks[1] = Material.OBSIDIAN;
        this.blocks[2] = Material.LIGHT_BLUE_TERRACOTTA;
        this.blocks[3] = Material.BLUE_TERRACOTTA;
        this.blocks[4] = Material.PURPLE_CONCRETE;
        this.blocks[5] = Material.AIR;

        if (count <= 0) {
            throw new IllegalArgumentException("counter must be greater than 0");
        } else {
            this.count = count;
        }
    }

    private void spawnBlock(Location loc) {
        Material material = getRandom(blocks);
        if (material.equals(Material.AIR)) return;
        ArmorStand stand = player.getWorld().spawn(rift.toLocation(player.getWorld()).add(loc.getX(),loc.getY(),loc.getZ()), ArmorStand.class);
        stand.setSmall(true);
        stand.setGravity(false);
        stand.setInvisible(true);
        stand.setInvulnerable(true);

        FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(new Location(player.getWorld(), rift.toLocation(player.getWorld()).getX(), -66, rift.toLocation(player.getWorld()).getZ()), material.createBlockData());
        fallingBlock.setDropItem(false);
        stand.addPassenger(fallingBlock);
        stand.setVelocity(new Vector(0, 0.3, 0));
        stand.setGravity(true);
    }

    /**
     * Copyright (c) Redraskal 2017.
     * <p>
     * Please do not copy the code below unless you
     * have permission to do so from me.
     */

    public static Location center(Location location) {
        String x = "" + location.getX();
        String z = "" + location.getZ();
        if(x.contains(".")) x = x.substring(0, x.indexOf("."));
        if(z.contains(".")) z = z.substring(0, z.indexOf("."));
        x+=".5";
        z+=".5";
        location.setX(Double.parseDouble(x));
        location.setZ(Double.parseDouble(z));
        return resetRotation(location);
    }

    private static Location resetRotation(Location location) {
        location.setYaw(0f);
        location.setPitch(0f);
        return location;
    }
    /* End of snippet */

    private
    static Material getRandom(Material[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public void run() {
        count--;
        if ((count / 2.5)  == (double) count / 2.5 && count / 2.5 != 0) {
            for (double i = 0.0; i < 360.0; i += 0.1) {
                double angle = i * Math.PI / 180;
                Location loc = new Location(player.getWorld(), Math.round(count / 2.5 * Math.sin(angle) * 2) / 2.0, -0.9, Math.round(count / 2.5 * Math.cos(angle) * 2) / 2.0);
                if (!usedLocs.contains(center(loc))) {
                    usedLocs.add(center(loc));
                    spawnBlock(center(loc));
                }
            }
        }
        Collection<Entity> toPull = player.getWorld().getNearbyEntities(rift.toLocation(player.getWorld()), 8, 8, 8);
        for (Entity entity : toPull) {
            if ((entity instanceof Damageable) && !(entity instanceof Player) && !(entity instanceof ArmorStand) && ((CraftEntity) entity).getHandle().isAttackable()) {
                double dX = (entity.getLocation().getX() - rift.toLocation(player.getWorld()).getX())*-1;
                double dY = (entity.getLocation().getY() - (rift.toLocation(player.getWorld()).getY()));
                double dZ = (entity.getLocation().getZ() - rift.toLocation(player.getWorld()).getZ())*-1;
                entity.setVelocity(new Vector(dX*0.35, dY*0.04, dZ*0.35));
            } else if (entity instanceof ArmorStand && entity.getPassengers().size() == 1 && entity.getPassengers().get(0) instanceof FallingBlock && (entity.getTicksLived() >= 11 || count <= 0) ) {
                entity.getPassengers().get(0).remove();
                entity.remove();
            }
        }
        if (count <= 0) this.cancel();
    }
}
