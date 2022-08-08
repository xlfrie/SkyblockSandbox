package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class TerminatorHandler extends CustomItemBase {
    private final double cooldown = 0.1;
    private final CustomItems customItem = CustomItems.SLOT11;

    public TerminatorHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!player.getWorld().getName().startsWith(SBSandbox.sandboxName)) return;
        if (!action.equals(Action.LEFT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK) && !action.equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        if (item == null || !canUse(item, customItem, player)) return;
        double _cooldown = getCooldown(player, cooldown, customItem.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cWhoa! Slow down!"));
            return;
        }

        Location loc = player.getLocation().add(new Vector(0, 1.15, 0));
        loc.add(loc.getDirection().multiply(3));
        Location arr1endpoint = loc.clone().add(loc.getDirection().multiply(10));
        Vector arr1vec = arr1endpoint.toVector().subtract(loc.toVector());
        Arrow arr1 = player.getWorld().spawnArrow(loc, arr1vec, 3, 0);
        Vector arr2vec = arr1vec.add(loc.getDirection().clone().crossProduct(new Vector(0, 1, 0)).multiply(2));
        Arrow arr2 = player.getWorld().spawnArrow(loc, arr2vec, 3, 0);
        Vector arr3vec = arr1vec.add(loc.getDirection().clone().crossProduct(new Vector(0, -2, 0)).multiply(2));
        Arrow arr3 = player.getWorld().spawnArrow(loc, arr3vec, 3, 0);
        arr1.setShooter(player);
        arr2.setShooter(player);
        arr3.setShooter(player);
        arr1.setBounce(false);
        arr2.setBounce(false);
        arr3.setBounce(false);
        abilityRegister(player, customItem);
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) event.setCancelled(true);
    }

    @EventHandler
    public void hit(ProjectileHitEvent event) {
        if (event.getHitBlock() != null && event.getEntityType() == EntityType.ARROW)
            event.getEntity().remove();
        if (event.getHitEntity() != null) {
            Entity entity = event.getHitEntity();
            Projectile arrow = event.getEntity();
            if (!(arrow.getShooter() instanceof Player) || !(event.getEntityType() == EntityType.ARROW)) return;
            double damage = ((Arrow) arrow).getDamage();
            ((Arrow) arrow).setDamage(0D);
            ((Damageable) entity).damage(damage, (Player) arrow.getShooter());
            event.setCancelled(true);
        }
    }
}
