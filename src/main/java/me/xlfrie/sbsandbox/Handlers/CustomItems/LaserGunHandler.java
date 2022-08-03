package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.GuardianBeam;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.UUID;

public class LaserGunHandler extends CustomItemBase {
    public LaserGunHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (item == null || !canUse(item, CustomItems.SLOT15, player.getWorld()))
            return;
        Vector recoil = player.getEyeLocation().getDirection().multiply(-0.15);
        int dist = 200;
        Location endpoint = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(dist));
        RayTraceResult res = player.getWorld().rayTraceEntities(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(1)), player.getEyeLocation().getDirection(), dist);
        if (res != null && res.getHitEntity() instanceof Damageable) {
            if (res.getHitEntity().getUniqueId() == player.getUniqueId()) return;
            endpoint = res.getHitPosition().toLocation(player.getWorld());
            ((Damageable) res.getHitEntity()).damage(100, player);
        }
        player.setVelocity(player.getVelocity().add(recoil));
        UUID beam = GuardianBeam.spawn(player.getEyeLocation().add(0, -0.5, 0).add(player.getEyeLocation().getDirection().multiply(-1)), endpoint);
        player.getServer().getScheduler().scheduleSyncDelayedTask(SBSandbox.getInstance(), new Runnable() {
            public void run() {
                GuardianBeam.despawn(beam);
            }
        }, 2L);
    }
}
