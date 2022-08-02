package sbsandbox.sbsandbox;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import sbsandbox.sbsandbox.Handlers.CustomItems.*;
import sbsandbox.sbsandbox.Handlers.NPCs.NPCHandler;
import sbsandbox.sbsandbox.Handlers.NPCs.NPCRotationHandler;
import sbsandbox.sbsandbox.commands.*;
import sbsandbox.sbsandbox.Handlers.CustomItems.TerminatorHandler;
import sbsandbox.sbsandbox.Handlers.*;
import sbsandbox.sbsandbox.Interactions.player;
import sbsandbox.sbsandbox.Mobs.CustomNpc;

import java.util.*;

public final class SBSandbox extends JavaPlugin {
    public static SBSandbox instance = null;

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private static HashMap<ServerPlayer, UUID> npcs = new HashMap<ServerPlayer, UUID>();
    public static HashMap<Integer, Tuple<CustomNpc, ServerPlayer>> npcClazz = new HashMap<Integer, Tuple<CustomNpc, ServerPlayer>>();
    private static List<Integer> npcIDs = new ArrayList<>();

    public static void addNpc(ServerPlayer npc, int id, UUID p, CustomNpc npc_) {
        npcs.put(npc, p);
        npcIDs.add(id);
        npcClazz.put(id, new Tuple<>(npc_, npc));
    }

    public static HashMap<ServerPlayer, UUID> getNpcs() {
        return npcs;
    }

    public static String sandboxName = "Stinky";
    static ScoreboardManager manager;
    public static Scoreboard board;
    public static Team collision;
    public HashMap<UUID, PermissionAttachment> attachments = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        manager = Bukkit.getScoreboardManager();
        board = manager.getMainScoreboard();
        if (board.getTeam("collision") == null) collision = board.registerNewTeam("collision");
        else collision = board.getTeam("collision");
        collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        Bukkit.getWorld("world").setTime(0);
        if (Bukkit.getWorlds().size() < 2) Utils.sandboxWorld();
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (CustomNpc npc : CustomNpc.values()) {
                npc.spawn(player, true);
            }
        }
//        Register handlers
        new HyperionHandler(this);
        new GyrokineticWandHandler(this);
        new HealthHandler(this);
        new TerminatorHandler(this);
        new LandProtection(this);
        new GrapplingHookHandler(this);
        new NPCHandler(this);
        new MenuHandler(this);
        new SheepHandler(this);
        new HungerHandler(this);
        new NPCRotationHandler(this);
        new LaserGunHandler(this);
        new LocationHandler(this);

//        Register commands
        getCommand("loop").setExecutor(new LoopCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("npc").setExecutor(new NPCCommand());
        getCommand("stonk").setExecutor(new StonkCommand());
        getCommand("reset").setExecutor(new ResetCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("rank").setExecutor(new RankCommand());

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(this, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                int entityID = packet.getIntegers().read(0);

                for (Integer npc : npcIDs) {
                    if (npc != entityID) continue;
//                    EnumWrappers.Hand hand = packet.getEnumEntityUseActions().read(0).getHand();
                    EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
//                    if (hand == EnumWrappers.Hand.MAIN_HAND && action == EnumWrappers.EntityUseAction.INTERACT)
                    {
                        switch (npcClazz.get(entityID).getA()) {
                            case XLFRIE:
                                Bukkit.getScheduler().runTask(plugin, () -> player.xlfrieMenu(event.getPlayer()));
                                break;
                            case HYPIXEL:
                                Bukkit.getScheduler().runTask(plugin, () -> player.hypixelMenu(event.getPlayer()));
                                break;
                        }
                    }
                }
            }
        });
    }

    public static SBSandbox getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Map.Entry<ServerPlayer, UUID> serverPlayerUUIDEntry : npcs.entrySet()) {
            CraftPlayer craftPlayer = (CraftPlayer) Bukkit.getPlayer(serverPlayerUUIDEntry.getValue());
            if (craftPlayer == null) continue;
            ServerPlayer npc = serverPlayerUUIDEntry.getKey();

            ServerGamePacketListenerImpl ps = craftPlayer.getHandle().connection;
            ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));
            ps.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
        }
        npcs.clear();
        saveConfig();
        Bukkit.getLogger().info("Shutting down");
    }
}
