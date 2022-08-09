package me.xlfrie.sbsandbox.CustomItems;

import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public abstract class CustomItemBase implements Listener {

    private HashMap<UUID, HashMap<String, Long>> cooldowns = new HashMap<>();

    public abstract String getId();

    public abstract String localizedName();

    public abstract int cooldownDelay();

    public abstract Material getMaterial();

    public String cooldownFormat() {
        return "§cYou can use this ability in %d";
    }

    public CustomItemBase() {
        ItemUtils.registerItem(this);
        Bukkit.getPluginManager().registerEvents(this, SBSandbox.getInstance());
    }

    public boolean canUse(ItemStack item, Player player) {
        if (item == null) return true;
        NamespacedKey key = new NamespacedKey(SBSandbox.getInstance(), "sandbox-id");
        boolean buf = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING) == this.getId();
        if (buf && cooldowns.containsKey(player.getUniqueId()) && cooldowns.get(player.getUniqueId()).containsKey(this.getId()) && cooldowns.get(player.getUniqueId()).get(this.getId()) + this.cooldownDelay() * 1000L >= System.currentTimeMillis()) {
            buf = false;
            player.sendMessage(String.format("§cYou can use this ability in %d", (cooldowns.get(player.getUniqueId()).get(this.getId()) + this.cooldownDelay() * 1000L - System.currentTimeMillis()) / 1000));
        } else if (buf && !cooldowns.containsKey(player.getUniqueId())) {
            HashMap<String, Long> tmp = new HashMap<>();
            tmp.put(this.getId(), 0L);
            cooldowns.put(player.getUniqueId(), tmp);
        } else if (buf && !cooldowns.get(player.getUniqueId()).containsKey(this.getId())) {
            cooldowns.get(player.getUniqueId()).put(this.getId(), 0L);
        }
        return !buf;
    }

    public void abilityRegister(Player player) {
        cooldowns.get(player.getUniqueId()).put(this.getId(), System.currentTimeMillis());
    }

    public ItemMeta getMetadata(ItemStack itemStack) {
        ItemMeta m = itemStack.getItemMeta();
        m.setDisplayName(this.localizedName());
        NamespacedKey key = new NamespacedKey(SBSandbox.getInstance(), "sandbox-id");
        m.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.getId());
        m.setUnbreakable(true);
        return m;
    }

    public ItemStack genItem() {
        ItemStack item = new ItemStack(this.getMaterial(), 1);
        item.setItemMeta(getMetadata(item));
        return item;
    }
}
