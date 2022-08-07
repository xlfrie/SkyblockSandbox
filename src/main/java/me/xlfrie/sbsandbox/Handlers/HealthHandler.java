package me.xlfrie.sbsandbox.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import me.xlfrie.sbsandbox.SBSandbox;

import java.util.Objects;

public class HealthHandler implements Listener {
    public HealthHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void spawn(EntitySpawnEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Damageable) || e instanceof ArmorStand || !((CraftEntity) e).getHandle().isAttackable() || (e.getCustomName() != null && Objects.equals(e.getCustomName(), "jeb_"))) return;
        e.setCustomNameVisible(true);
        e.setCustomName(e.getName() + " §c❤ " + ((Damageable) e).getHealth() + "/" + Objects.requireNonNull(((Attributable) e).getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() + "");
    }

    @EventHandler
    public void dmg(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Damageable) || e instanceof ArmorStand || e.getName().equals("jeb_")) return;
        e.setCustomNameVisible(true);
        e.setCustomName(e.getName().split(" §c❤")[0] + " §c❤ " + Math.round((((Damageable) e).getHealth() - event.getDamage()) * 100.0) / 100.0 + "/" + Objects.requireNonNull(((Attributable) e).getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }
}
