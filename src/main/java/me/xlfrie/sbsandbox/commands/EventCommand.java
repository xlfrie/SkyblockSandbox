package me.xlfrie.sbsandbox.commands;

import me.xlfrie.sbsandbox.FunGameEvents.LeChonk;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length < 1) return true;

        new LeChonk(SBSandbox.getInstance()).spawn(player);
        return true;
    }
}
