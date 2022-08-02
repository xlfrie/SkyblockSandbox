package sbsandbox.sbsandbox.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import sbsandbox.sbsandbox.CustomItems;
import sbsandbox.sbsandbox.SBSandbox;

public class MenuHandler implements Listener {
    public MenuHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void invClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        int slot = event.getSlot();

        if (!event.getView().getTitle().startsWith("Menu")) return;
        switch (event.getView().getTitle()) {
            case "Menu > Items":
                boolean contains = false;
                event.setCancelled(true);
                for (CustomItems it : CustomItems.values()) if (it.name().equals("SLOT" + slot)) contains = true;
                if (!contains) return;
                player.getInventory().addItem(CustomItems.valueOf("SLOT" + slot).Item());
                break;
            case "Menu > Warps":
                event.setCancelled(true);
                if (slot != 13) break;
                for (World world : Bukkit.getWorlds()) {
                    Location bedLoc = new Location(world, -0.5, 0, 4);
                    bedLoc.setYaw(180);
                    if (world.getName().startsWith("Stinky_active_"))
                        player.teleport(bedLoc);
                }
                break;
        }
    }
}
