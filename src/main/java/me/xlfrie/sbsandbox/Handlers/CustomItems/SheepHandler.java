package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class SheepHandler extends CustomItemBase {
    private final double cooldown = 0.1;

    public SheepHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (item == null || !canUse(item, CustomItems.SLOT14, player)) return;
        double _cooldown = getCooldown(player, cooldown, CustomItems.SLOT14.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + (long) Math.ceil(_cooldown) + " seconds."));
            return;
        }
        Entity e = player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().multiply(3)), EntityType.SHEEP);
        e.setCustomName("jeb_");
        e.setVelocity(player.getLocation().getDirection().multiply(4));
        abilityRegister(player, CustomItems.SLOT14.id);
        if (action.equals(Action.RIGHT_CLICK_BLOCK)) event.setCancelled(true);
    }
}
