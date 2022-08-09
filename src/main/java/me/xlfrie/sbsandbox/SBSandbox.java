package me.xlfrie.sbsandbox;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.xlfrie.sbsandbox.CustomItems.Weapons.JasonSword;
import me.xlfrie.sbsandbox.Handlers.*;
import me.xlfrie.sbsandbox.Handlers.CustomItems.*;
import me.xlfrie.sbsandbox.Handlers.NPCs.MenuHandler;
import me.xlfrie.sbsandbox.Handlers.NPCs.NPCHandler;
import me.xlfrie.sbsandbox.Handlers.NPCs.NPCRotationHandler;
import me.xlfrie.sbsandbox.Interactions.player;
import me.xlfrie.sbsandbox.Mobs.CustomNpc;
import me.xlfrie.sbsandbox.commands.*;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class SBSandbox extends JavaPlugin {
    public static final List<Integer> npcIDs = new ArrayList<>();
    private static final HashMap<ServerPlayer, UUID> npcs = new HashMap<ServerPlayer, UUID>();
    public static SBSandbox instance = null;
    public static HashMap<Integer, Tuple<CustomNpc, ServerPlayer>> npcClazz = new HashMap<Integer, Tuple<CustomNpc, ServerPlayer>>();
    public static HashMap<Integer, ArmorStand> clickMeStands = new HashMap<Integer, ArmorStand>();
    public static String sandboxName = "Stinky";
    public static Scoreboard board;
    public static Team collision;
    public static HashMap<Rank, Team> rankTeams = new HashMap<>();
    public static HashMap<UUID, PermissionAttachment> attachments = new HashMap<>();
    static ScoreboardManager manager;

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void addNpc(ServerPlayer npc, int id, UUID p, CustomNpc npc_, ArmorStand cms) {
        npcs.put(npc, p);
        npcIDs.add(id);
        npcClazz.put(id, new Tuple<>(npc_, npc));
        clickMeStands.put(id, cms);
    }

    public static HashMap<ServerPlayer, UUID> getNpcs() {
        return npcs;
    }

    public static SBSandbox getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Bukkit.getWorld("world").setKeepSpawnInMemory(false);
        manager = Bukkit.getScoreboardManager();
        board = manager.getMainScoreboard();
        if (board.getTeam("collision") == null) collision = board.registerNewTeam("collision");
        else collision = board.getTeam("collision");

        for (Rank value : Rank.values()) {
            if (board.getTeam(value.toString()) == null) {
                rankTeams.put(value, board.registerNewTeam(value.toString()));
                rankTeams.get(value).setColor(value.getColor());
                rankTeams.get(value).setPrefix(value.getPrefix());
            } else rankTeams.put(value, board.getTeam(String.valueOf(value)));
        }

        collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        Bukkit.getWorld("world").setTime(0);
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (CustomNpc npc : CustomNpc.values()) {
                npc.spawn(player, true);
            }
        }
//        Bukkit.getScheduler().runTaskTimerAsynchronously(this, ActionBar::tick, 0L, 20L);
//        Register handlers
        new HyperionHandler(this);
        new GyrokineticWandHandler(this);
        new TerminatorHandler(this);
        new GrapplingHookHandler(this);
        new SheepHandler(this);
        new LaserGunHandler(this);
        new BombDetonatorHandler(this);
        new SpidersBootsHandler(this);
        new PortalGunHandler(this);

        new HealthHandler(this);
        new LandProtection(this);
        new NPCHandler(this);
        new MenuHandler(this);
        new HungerHandler(this);
        new NPCRotationHandler(this);
        new LocationHandler(this);
        new PermissionHandler(this);
        new DeathMessageHandler(this);
        new FancyExplosionHandler(this);
        new ChatHandler(this);

        new JasonSword();


//        Register commands
        getCommand("loop").setExecutor(new LoopCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("npc").setExecutor(new NPCCommand());
        getCommand("stonk").setExecutor(new StonkCommand());
        getCommand("reset").setExecutor(new ResetCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("rank").setExecutor(new RankCommand());
        getCommand("giveitem").setExecutor(new GiveItemCommand());

        if (Bukkit.getWorlds().size() < 2) {
            for (File file : Bukkit.getWorld("world").getWorldFolder().getParentFile().listFiles()) {
                if (file != null && file.isDirectory() && file.getName().startsWith("Stinky_active_")) {
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Utils.sandboxWorld();
        }

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(this, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                int entityID = packet.getIntegers().read(0);
                for (Map.Entry<Integer, Tuple<CustomNpc, ServerPlayer>> integerTupleEntry : npcClazz.entrySet()) {
                    if (integerTupleEntry.getKey() != entityID) continue;
                    EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
                    if ((action == EnumWrappers.EntityUseAction.INTERACT && packet.getEnumEntityUseActions().read(0).getHand() == EnumWrappers.Hand.MAIN_HAND) || action == EnumWrappers.EntityUseAction.ATTACK) {
                        switch (npcClazz.get(entityID).getA()) {
                            case XLFRIE:
                                Bukkit.getScheduler().runTask(plugin, () -> player.xlfrieMenu(event.getPlayer()));
                                break;
                            case HYPIXEL:
                                Bukkit.getScheduler().runTask(plugin, () -> player.hypixelMenu(event.getPlayer()));
                                break;
                            case LTT:
                            case LTT2:
                                Bukkit.getScheduler().runTask(plugin, () -> player.bodyguard(event.getPlayer()));
                                break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Map.Entry<ServerPlayer, UUID> serverPlayerUUIDEntry : npcs.entrySet()) {
            CraftPlayer craftPlayer = (CraftPlayer) Bukkit.getPlayer(serverPlayerUUIDEntry.getValue());
            if (craftPlayer == null) continue;
            ServerPlayer npc = serverPlayerUUIDEntry.getKey();

            ServerGamePacketListenerImpl ps = craftPlayer.getHandle().connection;
            ps.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
            ps.send(new ClientboundRemoveEntitiesPacket(clickMeStands.get(npc.getId()).getId()));
        }
        npcs.clear();
        saveConfig();
        Bukkit.getLogger().info("Shutting down");
    }
}
