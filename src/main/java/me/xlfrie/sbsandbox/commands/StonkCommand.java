package me.xlfrie.sbsandbox.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.xlfrie.sbsandbox.Mobs.CustomZombie;
import me.xlfrie.sbsandbox.Mobs.Ghost;
import me.xlfrie.sbsandbox.Mobs.Kraken;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class StonkCommand implements TabExecutor {
    private Material[] mats = {
            Material.WHITE_STAINED_GLASS,
            Material.RED_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.BLACK_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
    };

    public static HashMap<Integer, ServerPlayer> integerServerPlayerHashMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = ((Player) sender);
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "ghost":
                    ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(new Ghost(EntityType.CREEPER, ((CraftWorld) player.getWorld()).getHandle(), player.getLocation()));
                    break;
                case "kraken":
                    ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(new Kraken(EntityType.WITHER, ((CraftWorld) player.getWorld()).getHandle(), player.getLocation()));
                    break;
                case "terracotta":
                    CustomZombie zombie = new CustomZombie(player.getLocation(), Component.empty());
                    zombie.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.6);
                    zombie.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5);
                    ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(zombie, CreatureSpawnEvent.SpawnReason.CUSTOM);

                    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), color("&cTerracotta"));

                    gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwNjQ3MDY1NDE0NywKICAicHJvZmlsZUlkIiA6ICI0NWY3YTJlNjE3ODE0YjJjODAwODM5MmRmN2IzNWY0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfSnVzdERvSXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU3MGEyZDJhMzYyYWRlZWIwNWU2ZjI1ZWRjZDU0Y2FjMzM1ZjIyZjBlNDY2OWU5YzFlYmQ5YTAyNTczYmNhYiIKICAgIH0KICB9Cn0=", "UzJc0cZTJOxNLWXV7nopdpcMjqxIG2JPJwqBZ+XVu3uzCRhbVjkCeOoo1xD9v6JJ3CuclVEAS9Zjbtb7jOfZOY8xO4ejIaoqdqLg2QxIjdhuMacaOE8Ax1yHek0JBnbec+0hqC7XfDchLmuGLpVqGz5VBvCrtzgi0JFtuqt4hNUYNxIkXxpw2EmCvf4q9uMxTBKyEOTe6yT79ydlYBylPwRu9ISkruOk0dBJ0h1hgxFInep8/b+FbTLIq0lxSvYqKhSNGKZ8YIKGWW9wacqZaaX8of4ha1cjpoZ7h1RO7+h4uwf+svDyhZ++9eFvWu2xZervgkwW1pi2f8MN5OV6MWhGW83498aHGd5+IpT1BtGHDVpQBEC2NUZ7t6aCfnUtqhCYPxz4GhibndlX/kGGvSIdHC8NLAI2kjP84poqxZFj7vevPSdX0i7eWE1U4x1HYvAJro6v3Ae5kDOu8hpQxiZYaAfq6w3plEdGrg5TFk+jctIvmW1yyO2CBwIr+T9ITVkxcvYxC9wIMBcyY2hanQK+4/TpPAHCxNqN9sywmn+9vPwIjQSX7Saao1vrc8s5wBhDGeul+Fxwyc24AodC2PGso5UreXJuDD2JDSaQzIc1YsnGFfk6LBHqAZPEB4eeH0KTuQ5Wkx0XxL7D+7V+vUZvBeKDdHSHn9CtkCbRr2g="));

                    ServerPlayer serverPlayer = new ServerPlayer(((CraftWorld) player.getWorld()).getHandle().getServer(), ((CraftWorld) player.getWorld()).getHandle(), gameProfile, null);
                    serverPlayer.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                    serverPlayer.level = ((CraftWorld) player.getWorld()).getHandle();
                    serverPlayer.setId(zombie.getId());
                    ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, serverPlayer));
                    ((CraftPlayer) player).getHandle().connection.send(new ClientboundAddPlayerPacket(serverPlayer));
                    SynchedEntityData data = serverPlayer.getEntityData();
                    data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
                    ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityDataPacket(serverPlayer.getId(), data, true ));
                    integerServerPlayerHashMap.put(serverPlayer.getId(), serverPlayer);
                    break;
            }
            return true;
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(SBSandbox.getInstance(), () -> {
            player.getEquipment().setHelmet(new ItemStack(mats[(int) (Math.random() * (16))], 1));
        }, 0L, 5L);


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
