package me.xlfrie.sbsandbox.commands;

import me.xlfrie.sbsandbox.Mobs.CustomNpc;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        for (CustomNpc npc : CustomNpc.values()) {
            npc.spawn(p, true);
        }
        return true;
    }
}
