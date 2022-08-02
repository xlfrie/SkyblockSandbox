package sbsandbox.sbsandbox;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class warp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        for (World world : Bukkit.getWorlds()) {
            Location bedLoc = new Location(world, -0.5, 0, 4);
            bedLoc.setYaw(180);
            if (world.getName().startsWith("Stinky_active_"))
                player.teleport(bedLoc);
        }
        return true;
    }
}
