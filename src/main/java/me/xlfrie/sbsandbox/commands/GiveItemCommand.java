package me.xlfrie.sbsandbox.commands;

import me.xlfrie.sbsandbox.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveItemCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
            Player player = (Player) sender;
        if (player.hasPermission("sbsanbox.owner")) Bukkit.getLogger().info("St");
        if (args.length < 1) return true;
        try {
            CustomItems.valueOf(args[0]).Item();
        } catch (IllegalArgumentException e) {
            return true;
        }
        if (args.length >= 2 && Bukkit.getPlayer(args[1]) != null) player = Bukkit.getPlayer(args[1]);
        player.getInventory().addItem(CustomItems.valueOf(args[0]).Item());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> res = new ArrayList<>();
            for (CustomItems value : CustomItems.values()) {
                res.add(value.toString());
            }
            return res;
        } else if (args.length == 2) {
            return null;
        }
        return new ArrayList<>();
    }
}
