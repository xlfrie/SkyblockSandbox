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
        boolean buf = m.hasCustomModelData()
                && m.getCustomModelData() == customItem.id
                && player.getWorld().getName().startsWith(customItem.getWorld());
//        if (buf && customItem.getAbilityData().size() > 0 && !ActionBar.hasMana((int) customItem.getAbilityData().get(1), player)) {
//            player.sendMessage("§cNot enough mana!");
//            buf = false;
//        }
        return buf;
    }

    public void abilityRegister(Player player, CustomItems customItem) {
        cooldownHash.get(player.getUniqueId()).put(customItem.id, System.currentTimeMillis());
//        if (customItem.getAbilityData().size() >= 1) {
//            ActionBar.useMana((int) customItem.getAbilityData().get(1), player);
//            ActionBar.setMessageBuf(SBSandbox.color("&b-" + customItem.getAbilityData().get(1) + " Mana (&6" + customItem.getAbilityData().get(0) + "&b)"), player);
//        }
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
//            if (player.hasPermission("OWNER")) return 0;
            return time;
        }
        return 0;
    }
}
