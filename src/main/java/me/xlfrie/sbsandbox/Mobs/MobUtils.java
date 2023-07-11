package me.xlfrie.sbsandbox.Mobs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class MobUtils {
    public static final HashMap<Integer, ServerPlayer> integerServerPlayerHashMap = new HashMap<>();

    public static Entity spawnPlayerMob(Location location, Class<? extends Zombie> c) throws NoSuchMethodException {
        Entity puppetMaster = null;
        Class[] args = new Class[2];
        args[0] = Location.class;
        args[1] = Component.class;
        try {
            puppetMaster = c.getDeclaredConstructor(args).newInstance(location, Component.empty());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(puppetMaster, CreatureSpawnEvent.SpawnReason.CUSTOM);

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), color("&cTerracotta"));

        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwNjQ3MDY1NDE0NywKICAicHJvZmlsZUlkIiA6ICI0NWY3YTJlNjE3ODE0YjJjODAwODM5MmRmN2IzNWY0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfSnVzdERvSXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU3MGEyZDJhMzYyYWRlZWIwNWU2ZjI1ZWRjZDU0Y2FjMzM1ZjIyZjBlNDY2OWU5YzFlYmQ5YTAyNTczYmNhYiIKICAgIH0KICB9Cn0=", "UzJc0cZTJOxNLWXV7nopdpcMjqxIG2JPJwqBZ+XVu3uzCRhbVjkCeOoo1xD9v6JJ3CuclVEAS9Zjbtb7jOfZOY8xO4ejIaoqdqLg2QxIjdhuMacaOE8Ax1yHek0JBnbec+0hqC7XfDchLmuGLpVqGz5VBvCrtzgi0JFtuqt4hNUYNxIkXxpw2EmCvf4q9uMxTBKyEOTe6yT79ydlYBylPwRu9ISkruOk0dBJ0h1hgxFInep8/b+FbTLIq0lxSvYqKhSNGKZ8YIKGWW9wacqZaaX8of4ha1cjpoZ7h1RO7+h4uwf+svDyhZ++9eFvWu2xZervgkwW1pi2f8MN5OV6MWhGW83498aHGd5+IpT1BtGHDVpQBEC2NUZ7t6aCfnUtqhCYPxz4GhibndlX/kGGvSIdHC8NLAI2kjP84poqxZFj7vevPSdX0i7eWE1U4x1HYvAJro6v3Ae5kDOu8hpQxiZYaAfq6w3plEdGrg5TFk+jctIvmW1yyO2CBwIr+T9ITVkxcvYxC9wIMBcyY2hanQK+4/TpPAHCxNqN9sywmn+9vPwIjQSX7Saao1vrc8s5wBhDGeul+Fxwyc24AodC2PGso5UreXJuDD2JDSaQzIc1YsnGFfk6LBHqAZPEB4eeH0KTuQ5Wkx0XxL7D+7V+vUZvBeKDdHSHn9CtkCbRr2g="));

        ServerPlayer puppet = new ServerPlayer(((CraftWorld) location.getWorld()).getHandle().getServer(), ((CraftWorld) location.getWorld()).getHandle(), gameProfile);
        puppet.setPos(location.getX(), location.getY(), location.getZ());
        puppet.setServerLevel(((CraftWorld) location.getWorld()).getHandle());
        puppet.setId(puppetMaster.getId());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            spawnPlayerMob(location, onlinePlayer, puppet, puppetMaster);
        }

        integerServerPlayerHashMap.put(puppet.getId(), puppet);
        return puppetMaster;
    }

    private static void spawnPlayerMob(Location location, Player player, ServerPlayer puppet, Entity puppetMaster) {
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, puppet));
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundAddPlayerPacket(puppet));
        SynchedEntityData data = puppet.getEntityData();
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityDataPacket(puppet.getId(), data.packDirty()));
    }

    public static void dragonDeathEffect(Location location) {
        EnderDragon ed = new EnderDragon(EntityType.ENDER_DRAGON, ((CraftWorld) location.getWorld()).getHandle(), location);
        ed.level().addFreshEntity(ed);
    }

}
