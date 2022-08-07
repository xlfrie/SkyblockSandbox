package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {
    public ChatHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        event.setFormat("%1$s: %2$s");
        event.setMessage(event.getMessage().replace("&", "§"));
    }
}
