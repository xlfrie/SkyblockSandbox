package me.xlfrie.sbsandbox;

import io.netty.channel.*;
import me.xlfrie.sbsandbox.Events.PacketEvent;
import net.minecraft.network.Connection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Field;

public final class Utils {
    public static void copy(File in, File out) throws IOException {
        if (in.isDirectory()) {
            if (!out.exists()) out.mkdir();

            String[] files = in.list();

            if (files == null) return;
            for (String file : files) {
                File newSource = new File(in, file);
                File newDestination = new File(out, file);
                copy(newSource, newDestination);
            }
        } else {
            InputStream in_ = new FileInputStream(in);
            OutputStream out_ = new FileOutputStream(out);

            byte[] buf = new byte[1024];
            int length;

            while ((length = in_.read(buf)) > 0) out_.write(buf, 0, length);

            in_.close();
            out_.close();
        }
    }
    public static void sandboxWorld() {
        File worldFolder = new File(SBSandbox.getInstance().getDataFolder(), "gameMaps");
        File sourceFolder = new File(worldFolder, "Stinky");
        File sandboxFolder = new File(Bukkit.getWorldContainer().getParentFile(), "Stinky" + "_active_" + System.currentTimeMillis());

        try {
            Utils.copy(sourceFolder, sandboxFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        World bukkitWorld = Bukkit.createWorld(new WorldCreator(sandboxFolder.getName()));
        bukkitWorld.setAutoSave(false);
    }

    public static void injectPacket(Player player) throws NoSuchFieldException {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (SBSandbox.getInstance().isEnabled())
                    Bukkit.getScheduler().runTask(SBSandbox.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new PacketEvent(msg, PacketEvent.direction.INBOUND, ctx)));
                super.channelRead(ctx, msg);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                Bukkit.getServer().getPluginManager().callEvent(new PacketEvent(msg, PacketEvent.direction.OUTBOUND));
                super.write(ctx, msg, promise);
            }

        };

        Field field = ((CraftPlayer) player).getHandle().connection.getClass().getDeclaredField("h");
        field.setAccessible(true);
        Connection conn;
        try {
            conn = (Connection) field.get(((CraftPlayer) player).getHandle().connection);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(false);

        ChannelPipeline pipeline = conn.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getUniqueId().toString(), channelDuplexHandler);
    }

    public static void uninjectPacket(Player player) throws NoSuchFieldException {
        Field field = ((CraftPlayer) player).getHandle().connection.getClass().getField("h");
        field.setAccessible(true);
        Connection conn;
        try {
            conn = (Connection) field.get(((CraftPlayer) player).getHandle().connection);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(false);

        Channel channel = conn.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getUniqueId().toString());
            return null;
        });
    }

    public static Object getValue(Object obj, String str) {
        Field field;
        try {
            field = obj.getClass().getDeclaredField(str);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return  null;
        }
    }
}
