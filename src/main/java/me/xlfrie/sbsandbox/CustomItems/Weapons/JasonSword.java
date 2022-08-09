package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class JasonSword extends CustomItemBase {
    @Override
    public String getId() {
        return "JASONS_BLADE";
    }

    @Override
    public String localizedName() {
        return "§cJason's Blade";
    }

    @Override
    public int cooldownDelay() {
        return 10;
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_AXE;
    }

    @EventHandler
    public void ability(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (canUse(item, player)) return;
        player.sendMessage("Stinky");
        this.abilityRegister(player);
    }
}
