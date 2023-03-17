package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class JasonSword extends CustomItemBase {
    @Override
    public String getId() {
        return "JASONS_BLADE";
    }

    @Override
    public String localizedName() {
        return "§cJason's Blade";
    }

    @Override
    public double cooldownDelay() {
        return 10;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_AXE;
    }

    private final HashMap<String, Object> _itemData = new HashMap<>() {{
        put("manaCost", 100);
        put("abilityName", "Jason!");
        put("health", 100);
        put("intelligence", 1000);
    }};

    @Override
    public HashMap<String, Object> itemData() {
        return _itemData;
    }

    @Override
    public ItemMeta getMetadata(ItemStack itemStack) {
        ItemMeta m = super.getMetadata(itemStack);
        m.getPersistentDataContainer().set(new NamespacedKey(SBSandbox.getInstance(), "dmgMult"), PersistentDataType.DOUBLE, 500_000D);
        return m;
    }
}
