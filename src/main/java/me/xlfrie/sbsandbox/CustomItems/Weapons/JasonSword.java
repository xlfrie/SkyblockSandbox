package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (canUse(item, player)) {
            return;
        }
    }
}
