package me.xlfrie.sbsandbox;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public enum CustomItems {
    BOMBDETONATOR(SBSandbox.color("&6&l&kA&r&6&lBOMB DETONATOR&kA&r"), new ItemStack(Material.GOLDEN_HORSE_ARMOR, 1), 1001, "Stinky", "was nuked by"),
    SLOT10(SBSandbox.color("&6Hyperion"), new ItemStack(Material.IRON_SWORD, 1), 1, "Stinky", "was exploded to smithereens by"),
    SLOT11(SBSandbox.color("&6Terminator"), new ItemStack(Material.BOW, 1), 2, "Stinky", "was terminated by"),
    SLOT12(SBSandbox.color("&5Gyrokinetic Wand"), new ItemStack(Material.BLAZE_ROD), 3, "Stinky"),
    SLOT13(SBSandbox.color("&aGrappling Hook"), new ItemStack(Material.FISHING_ROD), 4, "Stinky"),
    SLOT14(SBSandbox.color("&cSheep"), new ItemStack(Material.MANGROVE_PROPAGULE), 5, "Stinky"),
    SLOT15(SBSandbox.color("&6L.A.S.E.R. Gun"), new ItemStack(Material.DIAMOND_HORSE_ARMOR), 6, "Stinky", "was beamed by"),
    SLOT16(SBSandbox.color("&9Spider's Boots"), new ItemStack(Material.IRON_BOOTS), 7, "Stinky"),
    SLOT19(SBSandbox.color("&dPortal Gun"), new ItemStack(Material.ORANGE_CANDLE), 8, "Stinky");

    public final int id;
    private final String name;
    private final ItemStack item;
    private final String world;
    private final String deathMsg;

    CustomItems(String name, ItemStack item, int id, String world, String... deathMsg) {
        this.name = name;
        this.item = item;
        this.id = id;
        this.world = world;
        this.deathMsg = (deathMsg.length >= 1) ? deathMsg[0] : "got ez'd on by";
    }

    public ItemStack Item() {
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(SBSandbox.color(name));
        m.setCustomModelData(id);
        m.setUnbreakable(true);
        NamespacedKey key = new NamespacedKey(SBSandbox.getInstance(), "anti-stack");
        m.getPersistentDataContainer().set(key, PersistentDataType.STRING, UUID.randomUUID().toString());
        item.setItemMeta(m);
        return item;
    }

    public String getWorld() {
        return this.world;
    }

    public String getDeathMsg() {
        return this.deathMsg;
    }
}
