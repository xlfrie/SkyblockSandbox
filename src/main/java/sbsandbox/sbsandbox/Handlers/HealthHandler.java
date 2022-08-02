package sbsandbox.sbsandbox.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import sbsandbox.sbsandbox.SBSandbox;

public class HealthHandler implements Listener {
    public HealthHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void spawn(EntitySpawnEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Damageable) || e instanceof ArmorStand || e.getCustomName() == "jeb_" || !((CraftEntity) e).getHandle().isAttackable()) return;
        e.setCustomNameVisible(true);
        e.setCustomName(e.getName() + " §c❤ " + ((Damageable) e).getHealth() + "/" + ((Damageable) e).getMaxHealth() + "");
    }

    @EventHandler
    public void dmg(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Damageable) || e instanceof ArmorStand || e.getCustomName() == "jeb_") return;
        e.setCustomNameVisible(true);
        e.setCustomName(e.getName().split(" §c❤")[0] + " §c❤ " + Math.round((((Damageable) e).getHealth() - event.getDamage()) * 100.0) / 100.0 + "/" + ((Damageable) e).getMaxHealth());
    }
}
