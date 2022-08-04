package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class GrapplingHookHandler extends CustomItemBase {
    private final double cooldown = 2.0;
    static {
        cooldownHash = new HashMap<>();
    }


    public GrapplingHookHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void cast(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getState().equals(PlayerFishEvent.State.FISHING) || !canUse(item, CustomItems.SLOT13, player)) return;
        double _cooldown = getCooldown(player, cooldown, CustomItems.SLOT13.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + _cooldown + " seconds."));
            return;
        }
        Vector vec = hook.getLocation().toVector().subtract(player.getLocation().toVector());
        vec.multiply(new Vector(0.5, 0, 0.5));
        vec.setY(1);
        player.setVelocity(vec);
        cooldownRegister(player, CustomItems.SLOT13.id);
    }
}
