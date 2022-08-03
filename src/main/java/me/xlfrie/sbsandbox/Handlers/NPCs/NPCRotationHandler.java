package me.xlfrie.sbsandbox.Handlers.NPCs;

import me.xlfrie.sbsandbox.Mobs.CustomNpc;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

public class NPCRotationHandler implements Listener {
    public NPCRotationHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (Map.Entry<Integer, Tuple<CustomNpc, ServerPlayer>> integerCustomNpcEntry : SBSandbox.npcClazz.entrySet()) {
            //noinspection SwitchStatementWithTooFewBranches
            switch (integerCustomNpcEntry.getValue().getA()) {
                default:
                    ServerPlayer npc = integerCustomNpcEntry.getValue().getB();
                    ServerGamePacketListenerImpl ps = ((CraftPlayer) player).getHandle().connection;
                    if (npc.level != ((CraftWorld) player.getWorld()).getHandle()) return;
                    Location loc = npc.getBukkitEntity().getLocation();
                    loc.setDirection(player.getLocation().subtract(loc).toVector());
                    float yaw = loc.getYaw();
                    float pitch = loc.getPitch();
                    ps.send(new ClientboundRotateHeadPacket(npc, (byte) ((yaw%360)*256/360)));
                    ps.send(new ClientboundMoveEntityPacket.Rot(npc.getId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));
                    break;
            }
        }
    }
}
