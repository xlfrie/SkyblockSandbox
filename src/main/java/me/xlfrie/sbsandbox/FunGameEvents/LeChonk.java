package me.xlfrie.sbsandbox.FunGameEvents;

import me.xlfrie.sbsandbox.SBSandbox;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class LeChonk implements Listener {

    public static Pig[] pigs = new Pig[8];

    public LeChonk(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void spawn(Player player) {
        EnderDragon edrag = new EnderDragon(EntityType.ENDER_DRAGON, ((CraftWorld) player.getWorld()).getHandle());
        edrag.setInvisible(true);
        edrag.setPos(new Vec3(0, -77, 0));
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundAddEntityPacket(edrag));
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundEntityEventPacket(edrag, (byte) 3));

        SBSandbox.getInstance().getServer().getScheduler().runTaskLater(SBSandbox.getInstance(), () -> ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(edrag.getId())), 200);

        new Circling(player.getWorld()).runTaskTimer(SBSandbox.getInstance(), 0, 1);

    }

    @EventHandler
    public  void invuln (EntityDamageEvent event) {
        for (Pig pig : pigs) {
            if (pig == null) continue;
            if (event.getEntity().getEntityId() == pig.getEntityId()) event.setCancelled(true);
        }
    }

    class Circling extends BukkitRunnable {
        private int tick = 0;
        private float encapsulate = 15F;
        private float minEcaps = 1F;
        private World world;
        private double targetHeight = -56;
        private int stage = 150;

        public Circling(World level) {
            this.world = level;
            for (int i = 0; i < 8; i++) {
                pigs[i] = level.spawn(calculateLocation(i), Pig.class);
                pigs[i].setInvulnerable(true);
                pigs[i].setGravity(false);
                pigs[i].setAI(false);
                pigs[i].setCustomName("§6§lLeChonk");
            }
        }

        @Override
        public void run() {
            if (tick >= 200) {
                new Fountain(world).runTaskTimer(SBSandbox.getInstance(), 0, 2);
                for (Pig pig : pigs) {
                    pig.remove();
                }
                this.cancel();
            }
            for (int i = 0; i < 8; i++) {
                pigs[i].setInvulnerable(true);
                pigs[i].teleport(calculateLocation(i));
            }
            tick++;
        }

        private Location calculateLocation(int i) {
            float rad = minEcaps + (encapsulate - minEcaps) * (1 - tick / 200F);
            double angle = Math.PI / 4 * i + tick / 16F;
            Location loc = new Location(world, Math.sin(angle) * rad, -58, Math.cos(angle) * rad);
            if (tick > stage) {
                loc.add(0, (targetHeight + 58) * (tick - stage) / (200 - stage), 0);
            }
            loc.setDirection(loc.toVector().subtract(new Vector(0, loc.getY(), 0)));
            return loc;
        }
    }

    class Fountain extends BukkitRunnable {

        private final World world;
        private Location dropLoc;
        private int i = 0;
        private int e = 0;
        private Random random;
        private ItemEntity[] porkchops = new ItemEntity[400];

        public Fountain(World world) {
            this.world = world;

            this.dropLoc = new Location(world, 0, -56, 0);
            this.random = new Random();

            for (Player player : world.getPlayers()) {
                player.sendTitle("§6§lLeChonk is food.", null, 20, 10, 10);
            }
        }

        @Override
        public void run() {
            if (i < 50) {
                for (int j = 0; j < 8; j++) {
                    ItemEntity porkchop = new ItemEntity(((CraftWorld) world).getHandle(), 0, -56, 0, CraftItemStack.asNMSCopy(new ItemStack(Material.PORKCHOP)));
                    porkchop.setDeltaMovement(random.nextFloat(-0.3F, 0.3F), random.nextFloat(0, 0.4F), random.nextFloat(-0.3F, 0.3F));

                    for (Player player : world.getPlayers()) {
                        ((CraftPlayer) player).getHandle().connection.send(new ClientboundAddEntityPacket(porkchop));
                        ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityDataPacket(porkchop.getId(), porkchop.getEntityData().packDirty()));
                        ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityMotionPacket(porkchop));
                    }

                    porkchops[e] = porkchop;
                    e++;
                }
            }

            if (i > 100) {
                for (ItemEntity porkchop : porkchops) {
                    if (porkchop == null) continue;
                    for (Player player : world.getPlayers()) {
                        ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(porkchop.getId()));
                    }
                }
                this.cancel();
            }
            i++;
        }
    }
}
