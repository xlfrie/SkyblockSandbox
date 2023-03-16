package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.Objects;

import static me.xlfrie.sbsandbox.SBSandbox.getInstance;

public class HealthHandler implements Listener {
    public HealthHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void spawn(EntitySpawnEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Damageable) || e instanceof ArmorStand || !((CraftEntity) e).getHandle().isAttackable() || (e.getCustomName() != null && Objects.equals(e.getCustomName(), "jeb_")) || Objects.equals(e.getCustomName(), "Sleeping Golem"))
            return;
        e.setCustomNameVisible(true);
        e.setCustomName("§c" + e.getName() + "§a " + ((Damageable) e).getHealth() + "§f/§a" + Objects.requireNonNull(((Attributable) e).getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() + "§c❤");
    }

    @EventHandler
    public void dmg(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Damageable) || e instanceof ArmorStand || e.getName().equals("jeb_")) return;
        ((LivingEntity) e).setMaximumNoDamageTicks(0);
        e.setCustomNameVisible(true);
        e.setCustomName(e.getName().split("§a ")[0] + "§a " + new DecimalFormat("#,###.00").format(Math.round((((Damageable) e).getHealth() - event.getFinalDamage()) * 100.0) / 100.0) + "§f/§a" + new DecimalFormat("#,###.00").format(Objects.requireNonNull(((Attributable) e).getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()) + "§c❤");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void dmg2(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER && ((Player) event.getDamager()).getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(getInstance(), "dmgMult"), PersistentDataType.DOUBLE)) {
            double dmg = event.getDamage() * ((Player) event.getDamager()).getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(getInstance(), "dmgMult"), PersistentDataType.DOUBLE);
            if (((Damageable) event.getEntity()).getHealth() - dmg < 0) {
                ((CraftEntity) event.getEntity()).getHandle().kill();
                return;
            }
            ((Damageable) event.getEntity()).setHealth(((Damageable) event.getEntity()).getHealth() - dmg);
//            ((Damageable) event.getEntity()).damage(0, event.getDamager());
            event.setDamage(0);
            ((LivingEntity) event.getEntity()).setNoDamageTicks(5);
//            event.setDamage(dmg);
        }
    }

    @EventHandler
    public void regenCancellation(EntityRegainHealthEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) event.setCancelled(true);
    }
}
