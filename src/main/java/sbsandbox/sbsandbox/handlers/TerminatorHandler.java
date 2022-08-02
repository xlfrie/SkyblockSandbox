package sbsandbox.sbsandbox.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import sbsandbox.sbsandbox.CustomItems;
import sbsandbox.sbsandbox.SBSandbox;

public class TerminatorHandler implements Listener {
    public TerminatorHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        if (!player.getWorld().getName().startsWith(SBSandbox.sandboxName)) return;
        if (!action.equals(Action.LEFT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_AIR) && action.equals(Action.RIGHT_CLICK_BLOCK) && !action.equals(Action.RIGHT_CLICK_AIR)) return;
        if (item == null || !item.getItemMeta().hasCustomModelData() || item.getItemMeta().getCustomModelData() != CustomItems.SLOT11.id) return;

        Location loc = player.getLocation().add(new Vector(0,1.15,0));
        loc.add(loc.getDirection().multiply(3));
        Location arr1endpoint = loc.clone().add(loc.getDirection().multiply(10));
        Vector arr1vec = arr1endpoint.toVector().subtract(loc.toVector());
        player.getWorld().spawnArrow(loc, arr1vec, 3, 0);
        Vector arr2vec = arr1vec.add(loc.getDirection().clone().crossProduct(new Vector(0,1,0)).multiply(2));
        player.getWorld().spawnArrow(loc, arr2vec, 3, 0);
        Vector arr3vec = arr1vec.add(loc.getDirection().clone().crossProduct(new Vector(0,-2,0)).multiply(2));
        player.getWorld().spawnArrow(loc, arr3vec, 3, 0);
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) event.setCancelled(true);
    }
}
