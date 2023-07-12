package me.xlfrie.sbsandbox.Handlers.Packets;

import me.xlfrie.sbsandbox.Events.PacketEvent;
import me.xlfrie.sbsandbox.SBSandbox;
import me.xlfrie.sbsandbox.Utils;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class NpcClick implements Listener {
    public NpcClick(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void packet(PacketEvent event) {
        if (event.getPacket() instanceof ServerboundInteractPacket) {

            ServerboundInteractPacket serverboundInteractPacket = (ServerboundInteractPacket) event.getPacket();
            int eid = Integer.parseInt(Utils.getValue(serverboundInteractPacket, "a").toString());
            Object interactionAction;
            Object interactDat;
            interactDat = Utils.getValue(serverboundInteractPacket, "b");
            interactionAction = Utils.getValue(interactDat, "a");
            if (interactionAction != null && Utils.getValue(interactDat, "b") != null || interactionAction != null && !((InteractionHand) interactionAction).name().equals("MAIN_HAND")) {
                return;
            }
            SBSandbox.npcClazz.get(eid).getA().interact(Bukkit.getPlayer(UUID.fromString(event.getCtx().name())), (ServerboundInteractPacket) event.getPacket());
        }
    }
}
