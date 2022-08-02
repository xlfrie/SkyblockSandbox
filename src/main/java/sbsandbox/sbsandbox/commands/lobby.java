package sbsandbox.sbsandbox.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class lobby implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        Location bedLoc = new Location(Bukkit.getWorld("world"), -0.5, 0, 4);
        bedLoc.setYaw(180);
        player.teleport(bedLoc);
        return true;
    }
}
