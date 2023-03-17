package me.xlfrie.sbsandbox.CustomItems.Weapons;

import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BombDetonator extends CustomItemBase {
    @Override
    public String getId() {
        return "BOMB_DETONATOR";
    }

    @Override
    public String localizedName() {
        return "§6§l§kA§r§6§lBOMB DETONATOR§kA§r";
    }

    private final HashMap<String, Object> _itemData = new HashMap<>() {{
        put("manaCost", 100);
        put("abilityName", "Nuke");
        put("health", 100);
        put("intelligence", 1000);
    }};

    @Override
    public HashMap<String, Object> itemData() {
        return _itemData;
    }

    @Override
    public double cooldownDelay() {
        return 0;
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_HORSE_ARMOR;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (canUse(item, player)) return;
        Location loc = player.getTargetBlock(null, 200).getLocation();
        if (loc.getBlock().getBlockData().getMaterial() == Material.AIR) return;
        ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
        level.explode(new Illusioner(EntityType.ILLUSIONER, level), DamageSource.playerAttack(((CraftPlayer) player).getHandle()), new ExplosionDamageCalculator(), loc.getX(), loc.getY() + 1, loc.getZ(), 100, false, Level.ExplosionInteraction.BLOCK);
        abilityRegister(player);
    }
}
