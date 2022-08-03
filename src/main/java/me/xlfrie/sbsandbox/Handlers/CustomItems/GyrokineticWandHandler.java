package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.SBSandbox;
import me.xlfrie.sbsandbox.Tasks.GyroCancellation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class GyrokineticWandHandler extends CustomItemBase {
    public GyrokineticWandHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    private final double cooldown = 30.0;

    @EventHandler
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.LEFT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_AIR)) return;
        if (item == null || !canUse(item, CustomItems.SLOT12, player)) return;
        double _cooldown = getCooldown(player, cooldown);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + _cooldown + " seconds."));
            return;
        }
        RayTraceResult ray = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 26);
        if (ray != null) {
            Vector rift = ray.getHitBlock().getLocation().add(0.5, 0, 0.5).toVector();
            int count = 20;
            cooldownRegister(player);
            BukkitTask task = new GyroCancellation(SBSandbox.getInstance(), count, player, rift).runTaskTimer(SBSandbox.getInstance(), 0, 2);
        } else {
            player.sendMessage("§cYou must aim at the floor.");
        }
        if (action.equals(Action.LEFT_CLICK_BLOCK)) event.setCancelled(true);
    }
}
