package me.xlfrie.sbsandbox.Handlers.NPCs;

import me.xlfrie.sbsandbox.Mobs.CustomNpc;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static me.xlfrie.sbsandbox.SBSandbox.clickMeStands;

public class NPCHandler implements Listener {
    public NPCHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void join_(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (CustomNpc npc : CustomNpc.values()) {
            npc.spawn(player, true);
        }
    }

    @EventHandler
    public void changeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
//        Despawn npcs from old world and respawn new ones
        for (Map.Entry<ServerPlayer, UUID> serverPlayerUUIDEntry : SBSandbox.getNpcs().entrySet()) {
            if (serverPlayerUUIDEntry.getValue() == player.getUniqueId()) {
                ServerPlayer npc = serverPlayerUUIDEntry.getKey();
                ServerGamePacketListenerImpl ps = ((CraftPlayer) player).getHandle().connection;
                ps.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(npc.getUUID())));
                ps.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
                ps.send(new ClientboundRemoveEntitiesPacket(clickMeStands.get(npc.getId()).getId()));
            }
        }
        for (CustomNpc npc : CustomNpc.values()) {
            npc.spawn(event.getPlayer(), true);
        }
    }
}
