package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class BombDetonatorHandler extends CustomItemBase {
    private final double cooldown = 1;


    public BombDetonatorHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (item == null || !canUse(item, CustomItems.BOMBDETONATOR, player)) return;
        double _cooldown = getCooldown(player, cooldown, CustomItems.BOMBDETONATOR.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + (long) Math.ceil(_cooldown) + " seconds."));
            return;
        }
        Location loc = player.getTargetBlock(null, 200).getLocation();
        if (loc.getBlock().getBlockData().getMaterial() == Material.AIR) return;
        ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
        level.explode(new Illusioner(EntityType.ILLUSIONER, level), DamageSource.playerAttack(((CraftPlayer) player).getHandle()), new ExplosionDamageCalculator(), loc.getX(), loc.getY() + 1, loc.getZ(), 40, false, Explosion.BlockInteraction.BREAK);
        cooldownRegister(player, CustomItems.BOMBDETONATOR.id);
    }
}
