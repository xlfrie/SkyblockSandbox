package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class MeteorThrowerHandler extends CustomItemBase {
    private final double cooldown = 0.25;


    public MeteorThrowerHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (item == null || !canUse(item, CustomItems.METEORTHROWER, player)) return;
        double _cooldown = getCooldown(player, cooldown, CustomItems.METEORTHROWER.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + _cooldown + " seconds."));
            return;
        }
        Fireball fireball = (Fireball) player.getWorld().spawnEntity(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(2)), EntityType.FIREBALL);
        fireball.setVelocity(player.getEyeLocation().getDirection().multiply(4));
        fireball.setYield(7);
        fireball.setShooter(player);
        fireball.setBounce(false);
        cooldownRegister(player, CustomItems.METEORTHROWER.id);
    }
}
