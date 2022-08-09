package me.xlfrie.sbsandbox;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class ActionBar {
    private static HashMap<UUID, List<Long>> plStats = new HashMap<>();
    private static HashMap<UUID, String> msgBuf = new HashMap<>();
    private static String space = "    ";

    public static void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            plData(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(genActionBar(player.getUniqueId(), player.getHealth(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), plStats.get(player.getUniqueId()).get(0), plStats.get(player.getUniqueId()).get(1))));
        }
    }

    private static void plData(Player player) {
        player.setHealthScale(40);
        loadData(player);
        if (plStats.containsKey(player.getUniqueId()) && !(plStats.get(player.getUniqueId()).get(0) >= plStats.get(player.getUniqueId()).get(1)))
            plStats.put(player.getUniqueId(), List.of((long) (plStats.get(player.getUniqueId()).get(0) + plStats.get(player.getUniqueId()).get(1) * 0.02), plStats.get(player.getUniqueId()).get(1)));
    }

    private static void loadData(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta m = item.getItemMeta();
        if (!m.hasCustomModelData() || !plStats.containsKey(player.getUniqueId())) return;
        for (CustomItem value : CustomItem.values()) {
            if (value.id == m.getCustomModelData() && value.getStatBoost().size() > 1) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(value.getStatBoost().get(0) + 100);
                plStats.put(player.getUniqueId(), List.of(plStats.get(player.getUniqueId()).get(0), (long) value.getStatBoost().get(1) + 100));
            }
        }
    }

    private static String genActionBar(UUID plUUID, double health, double maxHealth, Long mana, Long maxMana) {
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
        plStats.put(player.getUniqueId(), List.of(plStats.get(player.getUniqueId()).get(0) - mana, 100L));
    }

    public static boolean hasMana(int mana, Player player) {
        return plStats.get(player.getUniqueId()).get(0) >= mana;
    }

    public static void setMessageBuf(String msg, Player player) {
        msgBuf.put(player.getUniqueId(), msg);
    }
}
