package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import me.xlfrie.sbsandbox.GuardianBeam;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Location;
import org.bukkit.Material;
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

import java.util.HashMap;
import java.util.UUID;

public class LaserGun extends CustomItemBase {
    @Override
    public String getId() {
        return "LASER_GUN";
    }

    @Override
    public String localizedName() {
        return "§6L.A.S.E.R. Gun";
    }

    @Override
    public double cooldownDelay() {
        return 0.5;
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_HORSE_ARMOR;
    }


    private final HashMap<String, Object> _itemData = new HashMap<>() {{
        put("manaCost", 100);
        put("abilityName", "Beam");
        put("health", 0);
        put("intelligence", 1000);
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
        player.getLocation().getBlockX();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (canUse(item, player)) {
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
            abilityRegister(player);
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
