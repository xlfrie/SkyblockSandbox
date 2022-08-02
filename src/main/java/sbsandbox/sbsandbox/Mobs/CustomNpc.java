package sbsandbox.sbsandbox.Mobs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sbsandbox.sbsandbox.SBSandbox;

import java.util.List;
import java.util.UUID;

public enum CustomNpc {

    HYPIXEL(SBSandbox.color("&cHypixel"),
            "o4Gg83ihSNCt3ZtvwEtG6uhi6fnm0JrdkHMPgCctqu1I72Z0gEcN+ZVtyZJS0Th52eYiSeCXI7ZZezPYoZCbW7pI/OiMma3rXZLLmSGvo+2SCcbkCa1Nrc565Y/x5GxZAdv3enGa4m1vV5AKn8toro6YDFgsGserp3Nn0P6YvjHUDknpbP60SiQ48Fhsqsep7rlBIqaLDE8/k5/TKn66d9MXseqTMttxLcPJ76RMeEr9V7XP+Y4lMYAy/y3s4b4sVVrlnuLp189GxceLEfA29yVLz0dL7OEROuNMKH1M98acEgnB7gRxCsZpC5R7jwxvayaMoRVTMV9DYb5H2k+ZZaEqKlFeG+VUBJDfqjL0zgLH8TQ/E+1/XotOOQmozZ8Hb9aVeI3XyLXKE2IcGi5/Xub65U6rHAc0Egzkq9hdrYKfhBeBlUNELJhjCV64yikbmrJQlT0g/eLAepFUOYM6/ydfDUBUPQUtQABbN6O+XY2R1kvu1nvZfPgqfMH03w0AICsiDdqnfTw1r9yyXnZ/TUPDGU+U92J7muC+RzuYOIgxOO2qfzxaLFdOcqnF+zBCHzb99D4dIuQ3KUwNqQAh6MftcBo5885LNOcF0K1mNFcSxecbcwUPu/dWu54tU1HvSnI78S2qHIj9GXaPnDMmXqHLeGzE6kQ/vbsrt5NaQ2I=",
            "ewogICJ0aW1lc3RhbXAiIDogMTY1ODUxNzQ0NDE0NiwKICAicHJvZmlsZUlkIiA6ICJmN2M3N2Q5OTlmMTU0YTY2YTg3ZGM0YTUxZWYzMGQxOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJoeXBpeGVsIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI0N2MwNTg4NDVmM2I2ZDAzNDhhYWU1NmI2YWExYTYxYzQ2OWQ4NGNiYWJhMDQ4MTY0MDUwYTFkMzM1ZDZlYTAiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE3OTEyNzkwZmYxNjRiOTMxOTZmMDhiYTcxZDBlNjIxMjkzMDQ3NzZkMGYzNDczMzRmOGE2ZWFlNTA5ZjhhNTYiCiAgICB9CiAgfQp9",
            List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_SWORD)))),
            -0.5,
            0,
            -0.5,
            "Stinky"),
    XLFRIE(SBSandbox.color("&cXLFrie"),
            "o4Gg83ihSNCt3ZtvwEtG6uhi6fnm0JrdkHMPgCctqu1I72Z0gEcN+ZVtyZJS0Th52eYiSeCXI7ZZezPYoZCbW7pI/OiMma3rXZLLmSGvo+2SCcbkCa1Nrc565Y/x5GxZAdv3enGa4m1vV5AKn8toro6YDFgsGserp3Nn0P6YvjHUDknpbP60SiQ48Fhsqsep7rlBIqaLDE8/k5/TKn66d9MXseqTMttxLcPJ76RMeEr9V7XP+Y4lMYAy/y3s4b4sVVrlnuLp189GxceLEfA29yVLz0dL7OEROuNMKH1M98acEgnB7gRxCsZpC5R7jwxvayaMoRVTMV9DYb5H2k+ZZaEqKlFeG+VUBJDfqjL0zgLH8TQ/E+1/XotOOQmozZ8Hb9aVeI3XyLXKE2IcGi5/Xub65U6rHAc0Egzkq9hdrYKfhBeBlUNELJhjCV64yikbmrJQlT0g/eLAepFUOYM6/ydfDUBUPQUtQABbN6O+XY2R1kvu1nvZfPgqfMH03w0AICsiDdqnfTw1r9yyXnZ/TUPDGU+U92J7muC+RzuYOIgxOO2qfzxaLFdOcqnF+zBCHzb99D4dIuQ3KUwNqQAh6MftcBo5885LNOcF0K1mNFcSxecbcwUPu/dWu54tU1HvSnI78S2qHIj9GXaPnDMmXqHLeGzE6kQ/vbsrt5NaQ2I=",
            "ewogICJ0aW1lc3RhbXAiIDogMTY1ODUxNzQ0NDE0NiwKICAicHJvZmlsZUlkIiA6ICJmN2M3N2Q5OTlmMTU0YTY2YTg3ZGM0YTUxZWYzMGQxOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJoeXBpeGVsIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI0N2MwNTg4NDVmM2I2ZDAzNDhhYWU1NmI2YWExYTYxYzQ2OWQ4NGNiYWJhMDQ4MTY0MDUwYTFkMzM1ZDZlYTAiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE3OTEyNzkwZmYxNjRiOTMxOTZmMDhiYTcxZDBlNjIxMjkzMDQ3NzZkMGYzNDczMzRmOGE2ZWFlNTA5ZjhhNTYiCiAgICB9CiAgfQp9",
            List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_SWORD)))),
            -0.5,
            0,
            -0.5,
            "world");

    public String name;
    private String signature;
    private String texture;
    private List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> items;
    private double x;
    private double y;
    private double z;
    public String world;

    CustomNpc(String name, String signature, String texture, List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> items, double x, double y, double z, String world) {
        this.name = name;
        this.signature = signature;
        this.texture = texture;
        this.items = items;
        this.x = x;
        this.y= y;
        this.z = z;
        this.world = world;
    }

    public void spawn(Player p, boolean add) {
        if (!p.getWorld().getName().startsWith(this.world)) return;
        CraftPlayer craftPlayer = (CraftPlayer) p;
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld) p.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer serverPlayer = new ServerPlayer(minecraftServer, serverLevel, gameProfile, null);
        serverPlayer.setPos(x, y, z);
        serverPlayer.level = serverLevel;

        ServerGamePacketListenerImpl ps = craftPlayer.getHandle().connection;
        serverPlayer.connection = new ServerGamePacketListenerImpl(serverPlayer.server, new Connection(PacketFlow.CLIENTBOUND), serverPlayer);
        ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, serverPlayer));
        ps.send(new ClientboundAddPlayerPacket(serverPlayer));
        ps.send(new ClientboundSetEquipmentPacket(serverPlayer.getBukkitEntity().getEntityId(), items));
        SynchedEntityData data = serverPlayer.getEntityData();
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
        ps.send(new ClientboundSetEntityDataPacket(serverPlayer.getId(), data, true ));
        if (!add) return;
        SBSandbox.addNpc(serverPlayer, serverPlayer.getId(), craftPlayer.getHandle().getUUID(), this);
    }
}
