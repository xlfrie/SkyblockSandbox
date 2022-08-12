package me.xlfrie.sbsandbox;

import me.xlfrie.sbsandbox.CustomItems.ItemUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class ActionBar {
    private static HashMap<UUID, HashMap<String, Object>> plStats = new HashMap<>();
    private static HashMap<UUID, String> msgBuf = new HashMap<>();
    private static String space = "    ";

    private static final HashMap<String, Object> defaults = new HashMap<>() {{
        put("maxHealth", 100);
        put("health", get("maxHealth"));
        put("maxMana", 100);
        put("mana", get("maxMana"));
    }};

    public static void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            plData(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(genActionBar(player.getUniqueId(), player.getHealth(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), (int) plStats.get(player.getUniqueId()).get("mana"), (int) plStats.get(player.getUniqueId()).get("maxMana"))));
        }
    }

    private static void plData(Player player) {
        player.setHealthScale(40);
        loadData(player);
        if (plStats.containsKey(player.getUniqueId()) && !((int) plStats.get(player.getUniqueId()).get("mana") >= (int) plStats.get(player.getUniqueId()).get("maxMana"))) {
            int manaCalculated = (int) ((int) plStats.get(player.getUniqueId()).get("mana") + (int) plStats.get(player.getUniqueId()).get("maxMana") * 0.05);
            plStats.get(player.getUniqueId()).put("mana", manaCalculated > (int) plStats.get(player.getUniqueId()).get("maxMana") ? manaCalculated - ( manaCalculated - (int) plStats.get(player.getUniqueId()).get("maxMana") ) : manaCalculated);
        }
    }

    private static void loadData(Player player) {
        if (!plStats.containsKey(player.getUniqueId())) plStats.put(player.getUniqueId(), defaults);
        int calculatedMaxMana = 100;
        ItemStack item = player.getInventory().getItemInMainHand();
        EntityEquipment equipment = player.getEquipment();
        for (ItemStack armorContent : equipment.getArmorContents()) {
            if (armorContent != null && armorContent.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(SBSandbox.getInstance(), "sandbox-id"), PersistentDataType.STRING)) {
                calculatedMaxMana += (int) ItemUtils.getItemClass(armorContent.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(SBSandbox.getInstance(), "sandbox-id"), PersistentDataType.STRING)).itemData().get("intelligence");
            }
        }
        if (item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(new NamespacedKey(SBSandbox.getInstance(), "sandbox-id"), PersistentDataType.STRING)) {
            calculatedMaxMana += (int) ItemUtils.getItemClass(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(SBSandbox.getInstance(), "sandbox-id"), PersistentDataType.STRING)).itemData().get("intelligence");
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((int) plStats.get(player.getUniqueId()).get("maxHealth"));
        int regenHealth = (int) (player.getHealth() + Math.floor(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.01 + 1.5 * 1.2));
        player.setHealth(regenHealth > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() ? player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() : regenHealth);
        plStats.get(player.getUniqueId()).put("maxMana", calculatedMaxMana);
    }

    private static String genActionBar(UUID plUUID, double health, double maxHealth, int mana, int maxMana) {
        String buf = "";
        buf += ChatColor.RED + String.format("%d/%d♥%s", (int) health, (int) maxHealth, space);
        if (msgBuf.containsKey(plUUID)) {
            buf += msgBuf.get(plUUID) + space;
            msgBuf.remove(plUUID);
        }
        buf += ChatColor.AQUA + String.format("%d/%d✎", mana, maxMana);
        return buf;
    }

    public static void useMana(int mana, Player player) {
        plStats.get(player.getUniqueId()).put("mana", Math.max((int) plStats.get(player.getUniqueId()).get("mana") - mana, 0));
    }

    public static boolean hasMana(int mana, Player player) {
        return (int) plStats.get(player.getUniqueId()).get("mana") >= mana;
    }

    public static void setMessageBuf(String msg, Player player) {
        msgBuf.put(player.getUniqueId(), msg);
    }
}
