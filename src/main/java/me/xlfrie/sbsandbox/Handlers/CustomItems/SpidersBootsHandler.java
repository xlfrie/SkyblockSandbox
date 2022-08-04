package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class SpidersBootsHandler extends CustomItemBase {
    private final double cooldown = 1;

    public SpidersBootsHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void flightToggle(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getEquipment().getBoots();

        if (player.getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
        if (item == null || !canUse(item, CustomItems.SLOT16, player)) return;
        double _cooldown = getCooldown(player, cooldown, CustomItems.SLOT16.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + (long) Math.ceil(_cooldown) + " seconds."));
            return;
        }
        player.setVelocity(player.getVelocity().setY(1));
        cooldownRegister(player, CustomItems.SLOT16.id);
    }
}
