package me.xlfrie.sbsandbox.Handlers.Packets;

import me.xlfrie.sbsandbox.Events.PacketEvent;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.UUID;

public class NpcClick implements Listener {
    public NpcClick(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void packet(PacketEvent event) throws Exception {
        if (event.getPacket() instanceof ServerboundInteractPacket) {
            ServerboundInteractPacket serverboundInteractPacket = (ServerboundInteractPacket) event.getPacket();
            Field field = serverboundInteractPacket.getClass().getDeclaredField("a");
            field.setAccessible(true);
            int eid = field.getInt(serverboundInteractPacket);
            SBSandbox.npcClazz.get(eid).getA().interact(Bukkit.getPlayer(UUID.fromString(event.getCtx().name())));
        }
    }
}
