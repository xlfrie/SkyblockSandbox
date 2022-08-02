package sbsandbox.sbsandbox.Handlers.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import sbsandbox.sbsandbox.CustomItems;
import sbsandbox.sbsandbox.SBSandbox;

public class SheepHandler implements Listener {
    public SheepHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (item == null || !item.getItemMeta().hasCustomModelData() || item.getItemMeta().getCustomModelData() != CustomItems.SLOT14.id)
            return;
        Entity e = player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().multiply(3)), EntityType.SHEEP);
        e.setCustomName("jeb_");
        e.setVelocity(player.getLocation().getDirection().multiply(4));
        if (action.equals(Action.RIGHT_CLICK_BLOCK)) event.setCancelled(true);
    }
}
