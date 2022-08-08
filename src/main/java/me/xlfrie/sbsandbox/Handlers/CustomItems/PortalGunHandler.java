package me.xlfrie.sbsandbox.Handlers.CustomItems;

import me.xlfrie.sbsandbox.CustomItems;
import me.xlfrie.sbsandbox.Mobs.NGInfFallingBlock;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

import static me.xlfrie.sbsandbox.SBSandbox.color;

public class PortalGunHandler extends CustomItemBase {
    private final double cooldown = 0;
    private CustomItems customItem = CustomItems.SLOT19;

    private HashMap<UUID, Tuple<Tuple<Location, FallingBlockEntity[]>, Tuple<Location, FallingBlockEntity[]>>> portals = new HashMap<>();

    public PortalGunHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.LEFT_CLICK_BLOCK))
            return;
        if (item == null || !canUse(item, customItem, player)) {
            return;
        }
        if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) event.setCancelled(true);
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            player.sendTitle(item.getType() == Material.ORANGE_CANDLE ? color("&9Blue") : color("&6Orange"), null, 0, 10, 5);
            item.setType(item.getType() == Material.ORANGE_CANDLE ? Material.BLUE_CANDLE : Material.ORANGE_CANDLE);
            return;
        }
        double _cooldown = getCooldown(player, cooldown, customItem.id);
        if (_cooldown > 0) {
            player.sendMessage(color("&cYou can use this ability in " + (long) Math.ceil(_cooldown) + " seconds."));
            return;
        }
        Material color = item.getType() == Material.ORANGE_CANDLE ? Material.ORANGE_CONCRETE_POWDER : Material.BLUE_CONCRETE_POWDER;
        RayTraceResult ray = player.rayTraceBlocks(20);
        if (ray == null || ray.getHitBlock() == null || ray.getHitBlockFace() == null) return;
        if (!portals.containsKey(player.getUniqueId()))
            portals.put(player.getUniqueId(), new Tuple<>(new Tuple<>(null, null), new Tuple<>(null, null)));
        if (color == Material.ORANGE_CONCRETE_POWDER) {
            if (portals.get(player.getUniqueId()).getA().getB() != null)
                for (FallingBlockEntity fallingBlockEntity : portals.get(player.getUniqueId()).getA().getB()) {
                    fallingBlockEntity.remove(Entity.RemovalReason.DISCARDED);
                }
        } else {
            if (portals.get(player.getUniqueId()).getB().getB() != null)
                for (FallingBlockEntity fallingBlockEntity : portals.get(player.getUniqueId()).getB().getB()) {
                    fallingBlockEntity.remove(Entity.RemovalReason.DISCARDED);
                }
        }
        Location loc = ray.getHitBlock().getLocation().add(ray.getHitBlockFace().getDirection()).subtract(0, 0, 0);
        loc.setDirection(player.getLocation().subtract(loc).toVector());
        loc.setYaw(loc.getYaw() - (loc.getYaw() % 90 >= 45 ? (90 - loc.getYaw() % 90) * -1 : loc.getYaw() % 90));
//        if (loc.getYaw() == 360) loc.setYaw(0);
        loc.setPitch(0);
        FallingBlockEntity[] fbes = new FallingBlockEntity[8];
        fbes[0] = spawnFallingBlock(player, loc, Material.BLACK_CONCRETE_POWDER, ray.getHitBlockFace());
        loc.add(0, 1, 0);
        fbes[1] = spawnFallingBlock(player, loc, color, ray.getHitBlockFace());
        loc.add(0, 1, 0);
        fbes[2] = spawnFallingBlock(player, loc, color, ray.getHitBlockFace());
        loc.add(0, 1, 0);
        fbes[3] = spawnFallingBlock(player, loc, Material.BLACK_CONCRETE_POWDER, ray.getHitBlockFace());
        loc.subtract(0, 1, 0);
        fbes[4] = spawnFallingBlock(player, loc, Material.BLACK_CONCRETE_POWDER, ray.getHitBlockFace(), 1);
        loc.subtract(0, 1, 0);
        fbes[5] = spawnFallingBlock(player, loc, Material.BLACK_CONCRETE_POWDER, ray.getHitBlockFace(), 1);
        fbes[6] = spawnFallingBlock(player, loc, Material.BLACK_CONCRETE_POWDER, ray.getHitBlockFace(), -1);
        loc.add(0, 1, 0);
        fbes[7] = spawnFallingBlock(player, loc, Material.BLACK_CONCRETE_POWDER, ray.getHitBlockFace(), -1);
        loc.subtract(0, 1, 0);
        abilityRegister(player, customItem);
        if (color == Material.ORANGE_CONCRETE_POWDER) {
            portals.get(player.getUniqueId()).getA().setA(loc);
            portals.get(player.getUniqueId()).getA().setB(fbes);
        } else {
            portals.get(player.getUniqueId()).getB().setA(loc);
            portals.get(player.getUniqueId()).getB().setB(fbes);
        }
    }

    private FallingBlockEntity spawnFallingBlock(Player player, Location loc, Material color, BlockFace face, double... offset) {
        Location _loc = loc.clone();
        if (offset.length >= 1) {
            _loc.setPitch(180);
            _loc.add(_loc.getDirection().clone().crossProduct(new Vector(0, 1, 0)).multiply(offset[0]));
        }
        FallingBlockEntity fbe = NGInfFallingBlock.fall(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(Math.round(_loc.getX()), Math.round(_loc.getY()), Math.round(_loc.getZ())), ((CraftBlockData) color.createBlockData()).getState());
        fbe.setNoGravity(true);
        fbe.dropItem = false;
        fbe.time = -2_147_483_647;
        fbe.setYRot(_loc.getYaw());
        ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(fbe);
        return fbe;
    }

    @EventHandler
    public void movement(PlayerMoveEvent event) {
        if (portals.containsKey(event.getPlayer().getUniqueId()) && portals.get(event.getPlayer().getUniqueId()).getA().getA() != null && portals.get(event.getPlayer().getUniqueId()).getB().getA() != null && portals.get(event.getPlayer().getUniqueId()).getA().getA().getWorld().equals(event.getPlayer().getWorld()) && portals.get(event.getPlayer().getUniqueId()).getB().getA().getWorld().equals(event.getPlayer().getWorld())) {
            if (portals.get(event.getPlayer().getUniqueId()).getA().getA().distance(event.getPlayer().getLocation()) <= 1.25) {
//                event.getPlayer().getWorld().loadChunk();
                event.getPlayer().teleport(portals.get(event.getPlayer().getUniqueId()).getB().getA().clone().add(portals.get(event.getPlayer().getUniqueId()).getB().getA().getDirection().multiply(1.5)).add(0, 0, 0.5));
            } else if (portals.get(event.getPlayer().getUniqueId()).getB().getA().distance(event.getPlayer().getLocation()) <= 1.25) {
                event.getPlayer().teleport(portals.get(event.getPlayer().getUniqueId()).getA().getA().clone().add(portals.get(event.getPlayer().getUniqueId()).getA().getA().getDirection().multiply(1.5)).add(0, 0, 0.5));
            }
        }
    }
}
