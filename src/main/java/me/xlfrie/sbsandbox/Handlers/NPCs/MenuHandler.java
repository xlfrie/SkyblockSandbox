package me.xlfrie.sbsandbox.Handlers.NPCs;

import me.xlfrie.sbsandbox.CustomItems.ItemUtils;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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
                event.setCancelled(true);
                
                if (inventory.getItem(slot) == null) return;
                ItemStack itemStack = inventory.getItem(slot);
                ItemMeta meta = itemStack.getItemMeta();
                String id =  meta.getPersistentDataContainer().get(new NamespacedKey(SBSandbox.getInstance(), "sandbox-id"), PersistentDataType.STRING);
                if (id == null) return;
                player.getInventory().addItem(ItemUtils.getItemClass(id).genItem());
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
        player.updateInventory();
    }
}
