package me.xlfrie.sbsandbox.commands;

import me.xlfrie.sbsandbox.Mobs.Ghost;
import me.xlfrie.sbsandbox.Mobs.Kraken;
import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StonkCommand implements TabExecutor {
    private Material[] mats = {
            Material.WHITE_STAINED_GLASS,
            Material.RED_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.BLACK_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = ((Player) sender);
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "ghost":
                    ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(new Ghost(EntityType.CREEPER, ((CraftWorld) player.getWorld()).getHandle(), player.getLocation()));
                    break;
                case "kraken":
                    ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(new Kraken(EntityType.WITHER, ((CraftWorld) player.getWorld()).getHandle(), player.getLocation()));
                    break;
            }
            return true;
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(SBSandbox.getInstance(), () -> {
            player.getEquipment().setHelmet(new ItemStack(mats[(int) (Math.random() * (16))], 1));
        }, 0L, 5L);


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
