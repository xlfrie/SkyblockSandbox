package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemBase implements Listener {
    //        TODO Add cooldowns to all items
    public boolean canUse(ItemStack item, CustomItems customItem, World world) {
        ItemMeta m = item.getItemMeta();
        if (m.hasCustomModelData() && m.getCustomModelData() == customItem.id && world.getName().startsWith(customItem.getWorld())) return true;
        return  false;
    }
}
