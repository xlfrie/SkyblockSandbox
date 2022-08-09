package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItem;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class SpidersBootsHandler extends CustomItemBase {
    private final double cooldown = 1;
    private CustomItem customItem = CustomItem.SLOT16;

    public SpidersBootsHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void flightToggle(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getEquipment().getBoots();

        if (player.getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
        else return;
        if (item == null || !canUse(item, customItem, player)) return;
        double _cooldown = getCooldown(player, cooldown, customItem.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + (long) Math.ceil(_cooldown) + " seconds."));
            return;
        }
        player.sendMessage(color("&cThis ability is disabled!"));
        return;
//        player.setVelocity(player.getVelocity().setY(1));
//        cooldownRegister(player, CustomItems.SLOT16.id);
    }

    @EventHandler
    public void flight(PlayerChangedWorldEvent event) {
        event.getPlayer().setAllowFlight(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void gamemode(PlayerGameModeChangeEvent event) {
        event.getPlayer().setAllowFlight(true);
    }
}
