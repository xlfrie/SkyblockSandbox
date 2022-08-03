package me.xlfrie.sbsandbox.commands;

import me.xlfrie.sbsandbox.SBSandbox;
import me.xlfrie.sbsandbox.Utils;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

public class ResetCommand implements CommandExecutor {
    private static HashMap<ServerPlayer, CraftPlayer> npcs;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        Location bedLoc = new Location(Bukkit.getWorld("world"), -0.5, 0, 4);
        bedLoc.setYaw(180);
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().startsWith("Stinky_active_")) {
                for (Player player : world.getPlayers()) {
                    player.sendMessage(SBSandbox.color("&c&lWARNING:&r&6 This world is restarting in 5 seconds! Use &a&l/lobby&r&6 to return to safety!"));
                }
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(SBSandbox.getInstance(), () -> {
            for (World world : Bukkit.getWorlds()) {
                if (world.getName().startsWith("Stinky_active_")) {
                    for (Player player : world.getPlayers()) {
                        player.setBedSpawnLocation(bedLoc, true);
                        player.teleport(bedLoc);
                    }
                    Bukkit.unloadWorld(world, false);
                    try {
                        FileUtils.deleteDirectory(world.getWorldFolder());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Utils.sandboxWorld();
        }, 100L);
        return true;
    }
}
