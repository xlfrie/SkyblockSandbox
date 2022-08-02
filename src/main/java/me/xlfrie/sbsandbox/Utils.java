package me.xlfrie.sbsandbox;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.*;

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
        File sandboxFolder = new File(Bukkit.getWorldContainer().getParentFile(), sourceFolder.getName() + "_active_" + System.currentTimeMillis());

        try {
            Utils.copy(sourceFolder, sandboxFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        World bukkitWorld = Bukkit.createWorld(new WorldCreator(sandboxFolder.getName()));
        bukkitWorld.setAutoSave(false);
    }
}
