package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;

public class MiscHandler implements Listener {
    public MiscHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void teleport(EntityTeleportEvent event) {
        if (event.getEntityType().equals(EntityType.ENDERMAN)) event.setCancelled(true);
    }
}
