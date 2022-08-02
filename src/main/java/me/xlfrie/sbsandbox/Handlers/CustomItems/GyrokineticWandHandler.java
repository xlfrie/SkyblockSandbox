package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.SBSandbox;
import me.xlfrie.sbsandbox.Tasks.GyroCancellation;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import me.xlfrie.sbsandbox.CustomItems;

public class GyrokineticWandHandler implements Listener {
    public GyrokineticWandHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    private BukkitTask spawnRift;
    private int count;
    @EventHandler
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.LEFT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_AIR)) return;
        if (item == null || !item.getItemMeta().hasCustomModelData() || item.getItemMeta().getCustomModelData() != CustomItems.SLOT12.id) return;

        RayTraceResult ray = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 26);
        if (ray != null) {
            Vector rift = ray.getHitBlock().getLocation().add(0.5,0,0.5).toVector();
            count = 20;

            BukkitTask task = new GyroCancellation(SBSandbox.getInstance(), count, player, rift).runTaskTimer(SBSandbox.getInstance(), 0, 2);
            } else {
                player.sendMessage("§cYou must aim at the floor.");
            }
        if (action.equals(Action.LEFT_CLICK_BLOCK)) event.setCancelled(true);
    }
}
