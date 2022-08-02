package sbsandbox.sbsandbox;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public enum Rank {
    NONE(ChatColor.GRAY.toString(), 0),
    FRIEND(ChatColor.DARK_AQUA + "[FRIEND]", 1),
    ADMIN(ChatColor.RED + "[ADMIN]", 2),
    OWNER(ChatColor.RED + "[OWNER]", 3);

    private final String prefix;
    private final Integer hierarchy;
    private final HashMap<Player, Rank> ranks = new HashMap<>();
    private final SBSandbox plugin = SBSandbox.getInstance();


    Rank(String prefix, Integer hierarchy) {
        this.prefix = prefix;
        this.hierarchy = hierarchy;
    }

    public void loadRank(Player player) {
        plugin.reloadConfig();
        String rankString = String.valueOf(plugin.getConfig().get(player.getUniqueId() + ".rank"));
        Bukkit.getLogger().info(rankString);
        Rank rank = (rankString.equals("null")) ? Rank.NONE : Rank.valueOf(rankString);
        player.setDisplayName(rank.prefix + " " + player.getName() + ChatColor.WHITE);
        player.setPlayerListName(rank.prefix + " " + player.getName() + ChatColor.WHITE);
        ranks.put(player, rank);
    }

    public void setRank(Player player) {
        ranks.put(player, this);
        saveRank(player);
        loadRank(player);
    }


    public void saveRank(Player player) {
        plugin.getConfig().set(player.getUniqueId() + ".rank", ranks.get(player).name());
        plugin.saveConfig();
        ranks.remove(player);
    }

    public Rank getRank(Player sender) {
        String rankString = String.valueOf(plugin.getConfig().get(sender.getUniqueId() + ".rank"));
        return Rank.valueOf(rankString);
    }

    public int getHierarchy() {
        return this.hierarchy;
    }
}

