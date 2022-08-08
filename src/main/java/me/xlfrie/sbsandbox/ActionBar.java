package me.xlfrie.sbsandbox;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

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
        if (plStats.containsKey(player.getUniqueId()) && !plStats.get(player.getUniqueId()).get(0).equals(plStats.get(player.getUniqueId()).get(1)))
            plStats.put(player.getUniqueId(), List.of((long) (plStats.get(player.getUniqueId()).get(0) * 1.02), 100L));
        else
            plStats.put(player.getUniqueId(), List.of(100L, 100L));
    }

    private static String genActionBar(UUID plUUID, double health, double maxHealth, Long mana, Long maxMana) {
        String buf = "";
        buf += ChatColor.RED + String.format("%d/%d♥%s", (int) health, (int) maxHealth, space);
        if (msgBuf.containsKey(plUUID)) buf += msgBuf.get(plUUID) + space;
        buf += ChatColor.AQUA + String.format("%d/%d✎", mana, maxMana);
        return buf;
    }

    public static void useMana(int mana, Player player) {
        plStats.put(player.getUniqueId(), List.of(plStats.get(player.getUniqueId()).get(0) - mana, 100L));
    }
    public static boolean hasMana(int mana, Player player) {
        return plStats.get(player.getUniqueId()).get(0) >= mana;
    }

}
