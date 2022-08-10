package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Sheep extends CustomItemBase {
    @Override
    public String getId() {
        return "SHEEP";
    }

    @Override
    public String localizedName() {
        return "§cSheep";
    }

    @Override
    public double cooldownDelay() {
        return 0;
    }

    @Override
    public Material getMaterial() {
        return Material.MANGROVE_PROPAGULE;
    }

    private HashMap<String, Object> _itemData = new HashMap<>() {{
        put("manaCost", 10);
        put("abilityName", "Jeb!");
        put("health", 0);
        put("intelligence", 10000000);
    }};

    @Override
    public HashMap<String, Object> itemData() {
        return _itemData;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (canUse(item, player)) return;
        Entity e = player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().multiply(3)), EntityType.SHEEP);
        e.setCustomName("jeb_");
        e.setVelocity(player.getLocation().getDirection().multiply(4));
        abilityRegister(player);
        if (action.equals(Action.RIGHT_CLICK_BLOCK)) event.setCancelled(true);
    }
}
