package me.xlfrie.sbsandbox;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;

import static me.xlfrie.sbsandbox.SBSandbox.rankTeams;

public enum Rank {
    NONE("", 0, ChatColor.GRAY),
    FRIEND("[FRIEND]", 1, ChatColor.DARK_AQUA),
    ADMIN("[ADMIN]", 2, ChatColor.RED),
    OWNER("[OWNER]", 3, ChatColor.GOLD);

    private final String prefix;
    private final Integer hierarchy;
    private final ChatColor color;
    private final HashMap<Player, Rank> ranks = new HashMap<>();
    private final SBSandbox plugin = SBSandbox.getInstance();


    Rank(String prefix, Integer hierarchy, ChatColor color) {
        this.prefix = prefix;
        this.hierarchy = hierarchy;
        this.color = color;
    }

    public void loadRank(Player player) {
        plugin.reloadConfig();
        String rankString = String.valueOf(plugin.getConfig().get(player.getUniqueId() + ".rank"));
        Bukkit.getLogger().info(rankString);
        if (rankString.equals("null")) NONE.setRank(player);
        if (!SBSandbox.attachments.containsKey(player.getUniqueId()))
            SBSandbox.attachments.put(player.getUniqueId(), player.addAttachment(SBSandbox.getInstance()));
        PermissionAttachment attach = SBSandbox.attachments.get(player.getUniqueId());
        if (getRank(player) != null) {
            attach.setPermission("sbsandbox." + rankString.toLowerCase(), true);
        }
        Rank rank = (rankString.equals("null")) ? NONE : Rank.valueOf(rankString);
        String rankPrefix = rank.color + rank.prefix + (rank.equals(NONE) ? "" : " ") + player.getName();
        rankTeams.get(rank).addEntry(player.getName());
        player.setDisplayName(rankPrefix + ChatColor.WHITE);
        player.setPlayerListName(rankPrefix + ChatColor.WHITE);
        ranks.put(player, rank);
    }

    public void setRank(Player player) {
        ranks.put(player, this);
        if (!SBSandbox.attachments.containsKey(player.getUniqueId()))
            SBSandbox.attachments.put(player.getUniqueId(), player.addAttachment(SBSandbox.getInstance()));
        PermissionAttachment attach = SBSandbox.attachments.get(player.getUniqueId());
        if (getRank(player) != null) {
            attach.setPermission("sbsandbox." + getRank(player).name().toLowerCase(), false);
        }
        saveRank(player);
        loadRank(player);
    }


    private void saveRank(Player player) {
        plugin.getConfig().set(player.getUniqueId() + ".rank", ranks.get(player).name());
        plugin.saveConfig();
        ranks.remove(player);
    }

    public Rank getRank(Player sender) {
        String rankString = String.valueOf(plugin.getConfig().get(sender.getUniqueId() + ".rank"));
        return (rankString.equals("null")) ? Rank.NONE : Rank.valueOf(rankString);
    }

    public int getHierarchy() {
        return this.hierarchy;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public String getPrefix() {
        return this.prefix + (this == NONE ? "" : " ");
    }
}

