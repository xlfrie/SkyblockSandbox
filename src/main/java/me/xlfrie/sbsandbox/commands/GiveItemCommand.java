package me.xlfrie.sbsandbox.commands;

import me.xlfrie.sbsandbox.CustomItems.ItemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length < 1) return true;

        player.getInventory().addItem(ItemUtils.getItemClass(args[0]).genItem());
        return true;
    }
}
