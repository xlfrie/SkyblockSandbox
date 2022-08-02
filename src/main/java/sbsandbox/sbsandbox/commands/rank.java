package sbsandbox.sbsandbox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import sbsandbox.sbsandbox.Rank;

import java.util.ArrayList;
import java.util.List;

import static sbsandbox.sbsandbox.SBSandbox.color;

public class rank implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = ((Player) sender);
        if (args.length == 0 && player.isOp()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Rank.NONE.loadRank(p);
            }
            return true;
        }
        if (Rank.NONE.getRank(player).getHierarchy() < 2) {
            player.sendMessage(color("&cYou don't have the permissions to do that!"));
            return true;
        }
        Rank rank_;
        try {
            rank_ = Rank.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            rank_ = Rank.NONE;
        }
        if (rank_.getHierarchy() >= Rank.NONE.getRank(player).getHierarchy()) {
            player.sendMessage(color("&cYou don't have the permissions to do that!"));
            return true;
        }
        if (args.length >= 2 && Bukkit.getPlayerExact(args[1]) != null) {
            rank_.setRank(Bukkit.getPlayerExact(args[1]));
            return true;
        }
        rank_.setRank(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> ranks = new ArrayList<>();
            for (Rank value : Rank.values()) {
                ranks.add(value.name());
            }
            return ranks;
        } else if (args.length == 2) {
            List<String> players = new ArrayList<>();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                players.add(onlinePlayer.getName());
            }
            return players;
        }
        return new ArrayList<>();
    }
}