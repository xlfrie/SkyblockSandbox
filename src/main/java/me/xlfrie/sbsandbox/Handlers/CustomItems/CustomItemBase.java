package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class CustomItemBase implements Listener {
    private static final HashMap<UUID, Long> cooldownHash = new HashMap<>();
    //        TODO Add cooldowns to all items
    public boolean canUse(ItemStack item, CustomItems customItem, Player player) {
        ItemMeta m = item.getItemMeta();
        return m.hasCustomModelData()
                && m.getCustomModelData() == customItem.id
                && player.getWorld().getName().startsWith(customItem.getWorld());
    }

    public void cooldownRegister(Player player) {
        cooldownHash.put(player.getUniqueId(), System.currentTimeMillis());
    }

    // TODO FIX YOU IDIOT
    public double getCooldown(Player player, double cooldown) {
        if (!cooldownHash.containsKey(player.getUniqueId())) {
            cooldownHash.put(player.getUniqueId(), 0L);
        }
        Bukkit.getLogger().info(String.valueOf(cooldownHash.containsKey(player.getUniqueId())));
        Bukkit.getLogger().info(String.valueOf(Math.ceil((cooldownHash.get(player.getUniqueId()) + (cooldown * 1000)) - System.currentTimeMillis())));
        if (cooldownHash.get(player.getUniqueId()) + (cooldown * 1000) - System.currentTimeMillis() > 0) {
            double time = (cooldownHash.get(player.getUniqueId()) + (cooldown * 1000) - System.currentTimeMillis()) / 1000;
            Bukkit.getLogger().info(String.valueOf(time < 0 ? 0 : time));
            return time < 0 ? 0 : time;
        }
        Bukkit.getLogger().info("hi");
        return 0;
    }
}
