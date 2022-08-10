package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

public class Hyperion extends CustomItemBase {
    @Override
    public String getId() {
        return "HYPERION";
    }

    @Override
    public String localizedName() {
        return "§6Hyperion";
    }

    @Override
    public double cooldownDelay() {
        return 0.1;
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    private HashMap<String, Object> _itemData = new HashMap<>() {{
        put("manaCost", 150);
        put("abilityName", "Wither Impact");
        put("health", 100);
        put("intelligence", 350);
    }};

    @Override
    public HashMap<String, Object> itemData() {
        return _itemData;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (canUse(item, player)) return;

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
        abilityRegister(player);
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
}
