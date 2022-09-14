package me.xlfrie.sbsandbox.Mobs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import me.xlfrie.sbsandbox.Interactions.player;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static me.xlfrie.sbsandbox.SBSandbox.collision;
import static me.xlfrie.sbsandbox.SBSandbox.color;

public enum CustomNpc {

    HYPIXEL(color("&cHypixel"),
            "o4Gg83ihSNCt3ZtvwEtG6uhi6fnm0JrdkHMPgCctqu1I72Z0gEcN+ZVtyZJS0Th52eYiSeCXI7ZZezPYoZCbW7pI/OiMma3rXZLLmSGvo+2SCcbkCa1Nrc565Y/x5GxZAdv3enGa4m1vV5AKn8toro6YDFgsGserp3Nn0P6YvjHUDknpbP60SiQ48Fhsqsep7rlBIqaLDE8/k5/TKn66d9MXseqTMttxLcPJ76RMeEr9V7XP+Y4lMYAy/y3s4b4sVVrlnuLp189GxceLEfA29yVLz0dL7OEROuNMKH1M98acEgnB7gRxCsZpC5R7jwxvayaMoRVTMV9DYb5H2k+ZZaEqKlFeG+VUBJDfqjL0zgLH8TQ/E+1/XotOOQmozZ8Hb9aVeI3XyLXKE2IcGi5/Xub65U6rHAc0Egzkq9hdrYKfhBeBlUNELJhjCV64yikbmrJQlT0g/eLAepFUOYM6/ydfDUBUPQUtQABbN6O+XY2R1kvu1nvZfPgqfMH03w0AICsiDdqnfTw1r9yyXnZ/TUPDGU+U92J7muC+RzuYOIgxOO2qfzxaLFdOcqnF+zBCHzb99D4dIuQ3KUwNqQAh6MftcBo5885LNOcF0K1mNFcSxecbcwUPu/dWu54tU1HvSnI78S2qHIj9GXaPnDMmXqHLeGzE6kQ/vbsrt5NaQ2I=",
            "ewogICJ0aW1lc3RhbXAiIDogMTY1ODUxNzQ0NDE0NiwKICAicHJvZmlsZUlkIiA6ICJmN2M3N2Q5OTlmMTU0YTY2YTg3ZGM0YTUxZWYzMGQxOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJoeXBpeGVsIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI0N2MwNTg4NDVmM2I2ZDAzNDhhYWU1NmI2YWExYTYxYzQ2OWQ4NGNiYWJhMDQ4MTY0MDUwYTFkMzM1ZDZlYTAiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE3OTEyNzkwZmYxNjRiOTMxOTZmMDhiYTcxZDBlNjIxMjkzMDQ3NzZkMGYzNDczMzRmOGE2ZWFlNTA5ZjhhNTYiCiAgICB9CiAgfQp9",
            List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.RED_WOOL))), new Pair<>(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.BLUE_WOOL)))),
            -0.5,
            0,
            -0.5,
            "Stinky",
            player::hypixelMenu),
    LTT(color("&6Bodyguard"),
            "mP8WAGFyhORY92DsAMWqYCWCAf/dU9mFQ5OoM2ZjAWOayiT5VdyUU5QiU3HAMot7Dj2e5beIAhGPRw7FkyfI4npCMJ/9CtK4lBZbttImyLROmsftJhnd4oHoqfl+z32R6TGXvN1u4hGD9Kl8D4JlcqM9OB/wbWTJA+OY3dL5wIr3mu/vW0T3Zj3o7MBE+KkMXLRhCDW911YwAXBQ5gVeuvv0/YqVCxpnzpgiJZcaFL4dvp0+zWBy+N9bBTZ0gPDwNgKwh8tEOjBcXLXW/8OdltHHRKWqif2etAtId/krg5h/Uz+q++Fld1TwXL5QWahBMSxhVYC9JOlYqNHoqmJ8dh2lHHGzztoc1Ch+CdITGeQQyMcRiCGhi/wTlbjBQvqCNG6gd0PwmqqWANsV+7hgVjAVRuiZWo2a6kxZKYQotywkQ+xJyoKTS/+cGU2Wv6FwYseXM7Ah3Yz09jtFw0Bb3+2mXjjUrZPIs3ItDF5DogyFG1fbbv+dKOjLiEp/eeBnVxmptUvo+L48S6aMUcx+4nsSbaFtkpD/uvOZzTOV3Tzjei3VPN9DTHNZLuBAIl2FhhgoUEMBTL71vf3tBPG8QYKZtQBAji6SiHMjbnDwLReUnBTLeZ0SVTaOVCydZWYXx1OgHD6YCy+1mWv3ZXfeovUWdO316VJ+E1vDGhNm3WU=",
            "ewogICJ0aW1lc3RhbXAiIDogMTY1OTU0NDc2NzcwMywKICAicHJvZmlsZUlkIiA6ICI4NGFkNDI5NzhmMjI0MDQzODkxZWY4Zjg2ZDBiYzhmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNck92YWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5YmQ2ODkwODExYTlmNzdkOGIzZGM0NTk1NDdlNmE1MWU3MGUyZGZiOGY3NjgzYzIyOGIxZmExMjNkZGM5YyIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0=",
            List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_SWORD))), new Pair<>(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.SHIELD)))),
            -2.5,
            0,
            -1,
            "Stinky",
            player::bodyguard),
    LTT2(color("&6Bodyguard"),
            "mP8WAGFyhORY92DsAMWqYCWCAf/dU9mFQ5OoM2ZjAWOayiT5VdyUU5QiU3HAMot7Dj2e5beIAhGPRw7FkyfI4npCMJ/9CtK4lBZbttImyLROmsftJhnd4oHoqfl+z32R6TGXvN1u4hGD9Kl8D4JlcqM9OB/wbWTJA+OY3dL5wIr3mu/vW0T3Zj3o7MBE+KkMXLRhCDW911YwAXBQ5gVeuvv0/YqVCxpnzpgiJZcaFL4dvp0+zWBy+N9bBTZ0gPDwNgKwh8tEOjBcXLXW/8OdltHHRKWqif2etAtId/krg5h/Uz+q++Fld1TwXL5QWahBMSxhVYC9JOlYqNHoqmJ8dh2lHHGzztoc1Ch+CdITGeQQyMcRiCGhi/wTlbjBQvqCNG6gd0PwmqqWANsV+7hgVjAVRuiZWo2a6kxZKYQotywkQ+xJyoKTS/+cGU2Wv6FwYseXM7Ah3Yz09jtFw0Bb3+2mXjjUrZPIs3ItDF5DogyFG1fbbv+dKOjLiEp/eeBnVxmptUvo+L48S6aMUcx+4nsSbaFtkpD/uvOZzTOV3Tzjei3VPN9DTHNZLuBAIl2FhhgoUEMBTL71vf3tBPG8QYKZtQBAji6SiHMjbnDwLReUnBTLeZ0SVTaOVCydZWYXx1OgHD6YCy+1mWv3ZXfeovUWdO316VJ+E1vDGhNm3WU=",
            "ewogICJ0aW1lc3RhbXAiIDogMTY1OTU0NDc2NzcwMywKICAicHJvZmlsZUlkIiA6ICI4NGFkNDI5NzhmMjI0MDQzODkxZWY4Zjg2ZDBiYzhmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNck92YWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5YmQ2ODkwODExYTlmNzdkOGIzZGM0NTk1NDdlNmE1MWU3MGUyZGZiOGY3NjgzYzIyOGIxZmExMjNkZGM5YyIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0=",
            List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_SWORD))), new Pair<>(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.SHIELD)))),
            1.5,
            0,
            -1,
            "Stinky",
            player::bodyguard),
    XLFRIE(color("&6XLFrie"),
            "d+8i54Ha0srY2mT+UkSlUe3XcH19Vef1iCd8OWupt83nVQQ0xbXJVH0hgxOhZbOql3vyjrfAGNqVZA32/KaX70+K3ee+3KVZmg6pd3MkMAlLaXNejgjL6i6Bl5pTtqPFg0LmESH9plNqM7WD1M2yoPK5srJEtwaapUEZlQtvuyJVjjXP7fx53GspS0dlTv+WDMNzucF0AdjQ8qSJlJmcdxWHyoAaqNN/PbOeqGxNIWw/akixFhJEc9hZHXjrdQ12HYZExRHdyUOCMWOLyBPVSC7oR5Hsxd34F4QSTn0Hk8gkFS9aXfhx8qS4EQUjyhFy6EuoogP0gk3UG5pFUT7oPOJnckcwssrkOJJvO8wEnhgiXdkU+t2YD7rWSOapOXG05++dmzQ3SbRlo1b7WyytGCSoZ28AnDYXhEigh5IowcWr2GtR7hj/oZMbBxlFlVZzWSAYqvPwM9HYraJXZkosYiIxaiWGzmdReVG96cErCcoZe16Ke4GHpWGXyjFlhxmnk77LExSjv0zhKJGUVX7jFbVhO7qO9Wfaut5kzVDSee+BWmOpA6bBJHGpKdC/MhIfeA8tMrGGaROPnWAYyri5R2uJp2BpRQq9jtTP6YRc+Y9aEPL4cSeqobHLIxweGMYqJVNKkN8XZnOksuLuO/2yRTCrs+osdWuWBgmRyZ0scvE=",
            "ewogICJ0aW1lc3RhbXAiIDogMTY1OTQ3OTIxODYxMSwKICAicHJvZmlsZUlkIiA6ICI3MWQ0OGQ4Mjg4MzE0ZmQwYmUxYWE2NzNjY2RhMmY5MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJYTEZyaWUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBiYmFiZjVkZmRhZTkzYWJlZGUzZmFmMTQ0NjI3ODY3YWEzZWFiMGIxZjFiMWI4YjkyNzIyMmM3ZDhmODUwZiIKICAgIH0KICB9Cn0=",
            List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_SWORD)))),
            -0.5,
            0,
            -0.5,
            "world",
            player::xlfrieMenu);

    public final String name;
    private final String signature;
    private final String texture;
    private final List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> items;
    private final double x;
    private final double y;
    private final double z;
    public final String world;
    private final InteractFunc function;

    CustomNpc(String name, String signature, String texture, List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> items, double x, double y, double z, String world, InteractFunc function) {
        this.name = name;
        this.signature = signature;
        this.texture = texture;
        this.items = items;
        this.x = x;
        this.y= y;
        this.z = z;
        this.world = world;
        this.function = function;
    }

    public interface InteractFunc {
        void func(Player p);
    }

    public void interact(Player p) {
        Bukkit.getScheduler().runTask(SBSandbox.getInstance(), () -> this.function.func(p));
    }

    public void spawn(Player p, boolean add) {
        if (!p.getWorld().getName().startsWith(this.world)) return;
//        if (this.toString() != "XLFRIE") return;
        CraftPlayer craftPlayer = (CraftPlayer) p;
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld) p.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), color("&c&lCLICK ME"));

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer serverPlayer = new ServerPlayer(minecraftServer, serverLevel, gameProfile, null);
        serverPlayer.setPos(x, y, z);
        serverPlayer.level = serverLevel;
        collision.addEntry(serverPlayer.getName().getString());

        ServerGamePacketListenerImpl ps = craftPlayer.getHandle().connection;
        serverPlayer.connection = new ServerGamePacketListenerImpl(serverPlayer.server, new Connection(PacketFlow.CLIENTBOUND), serverPlayer);
        ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, serverPlayer));
        ps.send(new ClientboundAddPlayerPacket(serverPlayer));
        ps.send(new ClientboundSetEquipmentPacket(serverPlayer.getBukkitEntity().getEntityId(), items));
        SynchedEntityData data = serverPlayer.getEntityData();
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
        ps.send(new ClientboundSetEntityDataPacket(serverPlayer.getId(), data, true ));
        ArmorStand clickMeStand = new ArmorStand(EntityType.ARMOR_STAND, serverLevel);
        clickMeStand.setPos(x, y+0.1, z);
        clickMeStand.setCustomName(Component.literal(name));
        clickMeStand.setCustomNameVisible(true);
        clickMeStand.setInvisible(true);
        ps.send(new ClientboundAddEntityPacket(clickMeStand));
        ps.send(new ClientboundSetEntityDataPacket(clickMeStand.getId(), clickMeStand.getEntityData(), true));
        Bukkit.getScheduler().runTaskLater(SBSandbox.getInstance(), () -> {
            ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, serverPlayer));
        }, 200L);
        if (!add) return;
        SBSandbox.addNpc(serverPlayer, serverPlayer.getId(), craftPlayer.getHandle().getUUID(), this, clickMeStand);
    }
}
