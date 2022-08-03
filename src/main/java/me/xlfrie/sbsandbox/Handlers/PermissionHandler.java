package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.Rank;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionHandler implements Listener {
    public PermissionHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPerms(player);
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        if (SBSandbox.attachments.containsKey(event.getPlayer().getUniqueId()))
            SBSandbox.attachments.remove(event.getPlayer().getUniqueId());
    }

    public void loadPerms(Player player) {
        if (!SBSandbox.attachments.containsKey(player.getUniqueId()))
            SBSandbox.attachments.put(player.getUniqueId(), player.addAttachment(SBSandbox.getInstance()));
        PermissionAttachment attach = SBSandbox.attachments.get(player.getUniqueId());
        attach.setPermission("sbsandbox." + Rank.NONE.getRank(player).name().toLowerCase(), true);
    }

    public void setPerms(Player player, String perm, boolean bool) {
        if (!SBSandbox.attachments.containsKey(player.getUniqueId()))
            SBSandbox.attachments.put(player.getUniqueId(), player.addAttachment(SBSandbox.getInstance()));
        PermissionAttachment attach = SBSandbox.attachments.get(player.getUniqueId());
        attach.setPermission(perm, bool);
    }
}
