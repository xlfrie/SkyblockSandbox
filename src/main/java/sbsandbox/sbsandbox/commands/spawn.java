package sbsandbox.sbsandbox.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sbsandbox.sbsandbox.mobs.CustomMobs;

public class spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        Location loc;
        if (args.length < 4) {
            loc = player.getLocation();
        } else {
            loc = new Location(player.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        }
        CustomMobs.valueOf(args[0]).spawn(loc);
        return true;
    }
}
