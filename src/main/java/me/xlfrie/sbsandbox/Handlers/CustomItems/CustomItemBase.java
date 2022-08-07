package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public abstract class CustomItemBase implements Listener {
    protected static HashMap<UUID, HashMap<Integer, Long>> cooldownHash = new HashMap<>();
    //        TODO Add cooldowns to all items
    public boolean canUse(ItemStack item, CustomItems customItem, Player player) {
        ItemMeta m = item.getItemMeta();
        return m.hasCustomModelData()
                && m.getCustomModelData() == customItem.id
                && player.getWorld().getName().startsWith(customItem.getWorld());
    }

    public void cooldownRegister(Player player, int id) {
        cooldownHash.get(player.getUniqueId()).put(id, System.currentTimeMillis());
    }

    public double getCooldown(Player player, double cooldown, int id) {
        if (!cooldownHash.containsKey(player.getUniqueId())) {
            HashMap<Integer, Long> tmp = new HashMap<>();
            tmp.put(id, 0L);
            cooldownHash.put(player.getUniqueId(), tmp);
        } else if (!cooldownHash.get(player.getUniqueId()).containsKey(id)) {
            cooldownHash.get(player.getUniqueId()).put(id, 0L);
        }
        if (cooldownHash.get(player.getUniqueId()).get(id) + (cooldown * 1000) - System.currentTimeMillis() > 0) {
            double time = (cooldownHash.get(player.getUniqueId()).get(id) + (cooldown * 1000) - System.currentTimeMillis()) / 1000;
            if (player.hasPermission("OWNER")) return 0;
            return time;
        }
        return 0;
    }
}
