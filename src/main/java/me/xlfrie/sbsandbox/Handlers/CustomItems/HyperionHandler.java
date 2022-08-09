package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItem;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.List;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class HyperionHandler extends CustomItemBase {
    private final double cooldown = 0.1;
    private CustomItem customItem = CustomItem.SLOT10;

    public HyperionHandler(SBSandbox plugin) {
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

        double tpdistance = 8;
        Location loc = player.getLocation();
        loc.add(0, 0.5, 0);
        Location infront = loc.clone().add(loc.getDirection().multiply(tpdistance));

//                Check collision
        RayTraceResult ray = player.getWorld().rayTraceBlocks(loc, loc.getDirection(), tpdistance);
        if (ray != null) {
            Location endpoint = ray.getHitPosition().toLocation(player.getWorld());
            endpoint.setDirection(loc.getDirection());
            endpoint.add(ray.getHitBlockFace().getDirection().multiply(new Vector(0.5, 1, 0.5)));
            if (ray.getHitBlockFace().getDirection().getY() == 1) endpoint.add(0, -1, 0);
            infront = endpoint;
        }
        player.teleport(infront);
        player.setVelocity(new Vector(0, 0, 0));
        abilityRegister(player, customItem);
//              Damage engine
        List<Entity> toDamage = player.getNearbyEntities(6, 6, 6);
        for (Entity entity : toDamage) {
            if (entity instanceof Damageable) ((Damageable) entity).damage(200, player);
            else if (entity instanceof Item) entity.remove();
        }
//              Particles, shield
        infront.add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 20, 2.5, 2.5, 2.5);
        player.getWorld().playSound(infront, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        player.setFallDistance(0);
        if (player.getAbsorptionAmount() == 0) {
            player.getWorld().playSound(infront, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 5));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));

            player.getServer().getScheduler().scheduleSyncDelayedTask(SBSandbox.getInstance(), new Runnable() {

                public void run() {
                    double change = player.getHealth() + player.getAbsorptionAmount();
                    player.setHealth(Math.min(change, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                    player.setAbsorptionAmount(0);
                    Location hearts = player.getLocation();
                    hearts.add(0, 0.5, 0);
                    player.getWorld().spawnParticle(Particle.HEART, hearts, 4, 0.5, 0.5, 0.5);
                }
            }, 60L);
        }
    }

    @EventHandler
    public void emanTp(EntityTeleportEvent event) {
        if (event.getEntityType().equals(EntityType.ENDERMAN)) {
            event.setCancelled(true);
        }
    }
}
