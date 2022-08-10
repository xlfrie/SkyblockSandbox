package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class GrapplingHook extends CustomItemBase {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cast(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getState().equals(PlayerFishEvent.State.FISHING) || canUse(item, player)) return;
        Vector vec = hook.getLocation().toVector().subtract(player.getLocation().toVector());
        vec.multiply(new Vector(0.5, 0, 0.5));
        vec.setY(1);
        player.setVelocity(vec);
    }

    @Override
    public String getId() {
        return "GRAPPLING_HOOK";
    }

    @Override
    public String localizedName() {
        return "§aGrappling Hook";
    }

    @Override
    public double cooldownDelay() {
        return 0;
    }

    @Override
    public Material getMaterial() {
        return Material.FISHING_ROD;
    }
    private HashMap<String, Object> _itemData = new HashMap<>() {{
        put("manaCost", 0);
        put("abilityName", "Grapple");
        put("health", 0);
        put("intelligence", 0);
    }};
    @Override
    public HashMap<String, Object> itemData() {
        return _itemData;
    }
}
