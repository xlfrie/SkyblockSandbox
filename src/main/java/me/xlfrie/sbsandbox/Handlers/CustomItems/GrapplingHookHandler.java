package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import me.xlfrie.sbsandbox.CustomItems;

public class GrapplingHookHandler implements Listener {
    public GrapplingHookHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void cast(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getState().equals(PlayerFishEvent.State.FISHING) || item == null || !item.getItemMeta().hasCustomModelData() || item.getItemMeta().getCustomModelData() != CustomItems.SLOT13.id) return;
        Vector vec = hook.getLocation().toVector().subtract(player.getLocation().toVector());
        vec.multiply(new Vector(0.5, 0, 0.5));
        vec.setY(1);
        player.setVelocity(vec);
    }
}
