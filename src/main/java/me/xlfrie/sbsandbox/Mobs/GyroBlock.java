package me.xlfrie.sbsandbox.Mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.block.data.CraftBlockData;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class GyroBlock {

    private static Material[] blocks = new Material[6];

    static {
        blocks[0] = Material.OBSIDIAN;
        blocks[1] = Material.OBSIDIAN;
        blocks[2] = Material.LIGHT_BLUE_TERRACOTTA;
        blocks[3] = Material.BLUE_TERRACOTTA;
        blocks[4] = Material.PURPLE_CONCRETE;
        blocks[5] = Material.AIR;
    }

    public GyroBlock(EntityType<? extends FallingBlockEntity> entitytypes, Level world, double x, double y, double z, Location to) {
        Vector vec = to.toVector().add(new Vector(new Random().nextDouble(0, 4), 0, new Random().nextDouble(0, 4))).subtract(new Vector(x, y - 1, z)).normalize().multiply(new Random().nextDouble(0, 0.2));
        ArmorStand stand = new GyroStand(world, x, y - 1, z);
        stand.setSmall(true);
        stand.noPhysics = true;
        stand.setInvisible(true);
        world.addFreshEntity(stand);
        FallingBlockEntity fb = GyroBlockEntity.fall(world, x, y - 1, z, ((CraftBlockData) blocks[new Random().nextInt(blocks.length)].createBlockData()).getState(), CreatureSpawnEvent.SpawnReason.DEFAULT);
        stand.addDeltaMovement(new Vec3(vec.getX(), new Random().nextDouble(0.2, 0.4), vec.getZ()));
        fb.startRiding(stand);
        fb.setNoGravity(true);
    }
}
