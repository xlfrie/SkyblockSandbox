package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import me.xlfrie.sbsandbox.SBSandbox;
import me.xlfrie.sbsandbox.Tasks.GyroCancellation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class GyrokineticWand extends CustomItemBase {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.LEFT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_AIR)) return;
        if (canUse(item, player)) return;
        RayTraceResult ray = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 26);
        if (ray != null) {
            Vector rift = ray.getHitBlock().getLocation().add(0.5, 0, 0.5).toVector();
            int count = 20;
            abilityRegister(player);
            BukkitTask task = new GyroCancellation(SBSandbox.getInstance(), count, player, rift).runTaskTimer(SBSandbox.getInstance(), 0, 2);
        } else {
            player.sendMessage("§cYou must aim at the floor.");
        }
        if (action.equals(Action.LEFT_CLICK_BLOCK)) event.setCancelled(true);
    }

    @Override
    public String getId() {
        return "GYROKINETIC_WAND";
    }

    @Override
    public String localizedName() {
        return "§5Gyrokinetic Wand";
    }

    @Override
    public double cooldownDelay() {
        return 30;
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_ROD;
    }

    private HashMap<String, Object> _itemData = new HashMap<>() {{
        put("manaCost", 1200);
        put("abilityName", "Gravity Storm");
        put("health", 0);
        put("intelligence", 0);
    }};

    @Override
    public HashMap<String, Object> itemData() {
        return _itemData;
    }
}
