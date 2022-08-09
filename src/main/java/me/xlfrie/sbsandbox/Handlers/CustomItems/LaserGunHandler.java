package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItem;
import me.xlfrie.sbsandbox.GuardianBeam;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.UUID;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class LaserGunHandler extends CustomItemBase {
    private final double cooldown = 0   ;
    private CustomItem customItem = CustomItem.SLOT15;
    public LaserGunHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (item == null || !canUse(item, customItem, player)) {
            return;
        }
        double _cooldown = getCooldown(player, cooldown, customItem.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + (long) Math.ceil(_cooldown) + " seconds."));
            return;
        }
        Vector recoil = player.getEyeLocation().getDirection().multiply(-0.15);
        int dist = 200;
        Location endpoint;
        RayTraceResult res = player.getWorld().rayTraceEntities(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(1)), player.getEyeLocation().getDirection(), dist, (e) -> e.getUniqueId() != player.getUniqueId() && !(e instanceof ArmorStand));
        if (res != null && res.getHitEntity() instanceof Damageable && res.getHitBlock() == null) {
            endpoint = res.getHitPosition().toLocation(player.getWorld());
            ((Damageable) res.getHitEntity()).damage(100, player);
            player.setVelocity(player.getVelocity().add(recoil));
            abilityRegister(player, customItem);
            player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ATTACK_IMPACT, 10, 2F);
            UUID beam = GuardianBeam.spawn(player.getEyeLocation().add(0, -0.5, 0).add(player.getEyeLocation().getDirection().multiply(-1)), endpoint);
            player.getServer().getScheduler().scheduleSyncDelayedTask(SBSandbox.getInstance(), new Runnable() {
                public void run() {
                    GuardianBeam.despawn(beam);
                }
            }, 2L);
        }
    }
}
