package me.xlfrie.sbsandbox.Interactions;

import me.xlfrie.sbsandbox.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class player {
    public static void hypixelMenu(Player player) {
        int size = 54;
        Inventory menu = Bukkit.createInventory(null, size, "Menu > Items");
        for (int i = 0; i < size; i++) {
//            Add border & filler
            menu.setItem(i, new ItemStack(i <= 8 || i >= size - 10 || i % 9 == 0 || (i + 1) % 9 == 0 ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE, 1));
        }

        for (CustomItem value : CustomItem.values()) {
            if (!value.toString().startsWith("SLOT")) continue;
            menu.setItem(Integer.parseInt(value.toString().substring(4)), value.Item());
        }

        player.openInventory(menu);
    }

    public static void xlfrieMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "Menu > Warps");
        ItemStack item = new ItemStack(Material.SAND, 1);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(color("&6Sandbox"));
        m.setLore(Arrays.asList(color("&8Sandbox is a mode where you can play"), color("&8 with Hypixel Skyblock & custom items!")));
        item.setItemMeta(m);
        menu.setItem(0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(1, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(2, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(3, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(4, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(5, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(7, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(8, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));

        menu.setItem(9, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(10, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(11, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(12, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(13, item);
        menu.setItem(14, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(15, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(16, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(17, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));

        menu.setItem(18, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(19, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(20, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(21, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(22, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(23, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(24, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(25, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        menu.setItem(26, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));

        player.openInventory(menu);
    }

    public static void bodyguard(Player player) {
        player.sendMessage(color("&e[NPC] &6Bodyguard&f: Bah humbug!"));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 0.65F);
    }
}
