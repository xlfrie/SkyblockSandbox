package sbsandbox.sbsandbox.commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class loop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        String command_ = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        for (int loop_ = 0; loop_ < Integer.valueOf(args[0]); loop_++) {
            Bukkit.getServer().dispatchCommand(sender, command_);
        }

        player.sendMessage("§cLoop finished");

        return true;
    }
}
