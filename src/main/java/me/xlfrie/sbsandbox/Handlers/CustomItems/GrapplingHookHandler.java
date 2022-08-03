package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GrapplingHookHandler extends CustomItemBase {
    public GrapplingHookHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void cast(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getState().equals(PlayerFishEvent.State.FISHING) || !canUse(item, CustomItems.SLOT13, player.getWorld())) return;
        Vector vec = hook.getLocation().toVector().subtract(player.getLocation().toVector());
        vec.multiply(new Vector(0.5, 0, 0.5));
        vec.setY(1);
        player.setVelocity(vec);
    }
}
