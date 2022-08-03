package me.xlfrie.sbsandbox;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public enum CustomItems {
    SLOT10(SBSandbox.color("&6Hyperion"), new ItemStack(Material.IRON_SWORD, 1), 1, "Stinky"),
    SLOT11(SBSandbox.color("&6Terminator"), new ItemStack(Material.BOW, 1), 2, "Stinky"),
    SLOT12(SBSandbox.color("&5Gyrokinetic Wand"), new ItemStack(Material.BLAZE_ROD), 3, "Stinky"),
    SLOT13(SBSandbox.color("&aGrappling Hook"), new ItemStack(Material.FISHING_ROD), 4, "Stinky"),
    SLOT14(SBSandbox.color("&cSheep"), new ItemStack(Material.MANGROVE_PROPAGULE), 5, "Stinky"),
    SLOT15(SBSandbox.color("&6L.A.S.E.R. Gun"), new ItemStack(Material.DIAMOND_HORSE_ARMOR), 6, "Stinky");

    private final String name;
    private final ItemStack item;
    public final int id;
    private final String world;

    CustomItems(String name, ItemStack item, int id, String world) {
        this.name = name;
        this.item = item;
        this.id = id;
        this.world = world;
    }

    public ItemStack Item() {
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(SBSandbox.color(name));
        m.setCustomModelData(id);
        m.setUnbreakable(true);
        NamespacedKey key = new NamespacedKey(SBSandbox.getInstance(), "our-custom-key");
        m.getPersistentDataContainer().set(key, PersistentDataType.STRING, UUID.randomUUID().toString());
        item.setItemMeta(m);
        return item;
    }

    public String getWorld() {
        return this.world;
    }
}
