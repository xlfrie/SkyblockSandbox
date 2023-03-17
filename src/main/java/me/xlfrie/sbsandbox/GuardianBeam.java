package me.xlfrie.sbsandbox;

import me.xlfrie.sbsandbox.Mobs.CustomGuardian;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

import java.util.HashMap;
import java.util.UUID;

public class GuardianBeam {
    public static final HashMap<UUID, Tuple<ArmorStand, CustomGuardian>> customEntities = new HashMap<UUID, Tuple<ArmorStand, CustomGuardian>>();
    public static UUID spawn(Location locFrom, Location locTo) {
        ServerLevel serverLevel = ((CraftWorld) locFrom.getWorld()).getHandle();
        ArmorStand stand = new ArmorStand(EntityType.ARMOR_STAND, serverLevel);
        stand.setPos(locTo.getX(), locTo.getY()-0.5, locTo.getZ());
        stand.setInvisible(true);
        stand.setInvulnerable(true);
        stand.setNoGravity(true);

        serverLevel.addFreshEntity(stand);
        CustomGuardian guardianBeam = new CustomGuardian(locFrom, stand);
        guardianBeam.setNoAi(true);
        guardianBeam.setSilent(true);
        guardianBeam.setInvisible(true);
        serverLevel.addFreshEntity(guardianBeam);
        guardianBeam.setActiveAttackTarget(stand.getId());
        guardianBeam.level.broadcastEntityEvent(guardianBeam, (byte) 21);
        UUID uuid = UUID.randomUUID();
        customEntities.put(uuid, new Tuple<>(stand, guardianBeam));
        return uuid;
    }

    public static void despawn(UUID uuid) {
        Tuple<ArmorStand, CustomGuardian> entities = customEntities.get(uuid);
        if (entities == null) throw new RuntimeException("UUID not found.");
        customEntities.remove(entities);
        ArmorStand stand = entities.getA();
        CustomGuardian customGuardian = entities.getB();
        if (!stand.isAlive() && !customGuardian.isAlive()) throw new RuntimeException("Entities are not alive.");
        stand.remove(Entity.RemovalReason.DISCARDED);
        customGuardian.remove(Entity.RemovalReason.DISCARDED);
    }
}
