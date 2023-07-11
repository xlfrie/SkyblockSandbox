package me.xlfrie.sbsandbox.Mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Iterator;

public class EnderDragon extends net.minecraft.world.entity.boss.enderdragon.EnderDragon {

    public EnderDragon(EntityType<? extends net.minecraft.world.entity.boss.enderdragon.EnderDragon> entitytypes, Level world, Location location) {
        super(entitytypes, world);
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setNoAi(true);
    }

    public void aiStep() {
        this.setHealth(0);
        Bukkit.getLogger().info(String.valueOf(this.position()));
    }

    @Override
    protected void tickDeath() {
        if (this.getDragonFight() != null) {
            this.getDragonFight().updateDragon(this);
        }

        ++this.dragonDeathTime;

        int short0 = this.expToDrop;
        if (this.level() instanceof ServerLevel) {
            if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0) {
                ExperienceOrb.award((ServerLevel) this.level(), this.position(), Mth.floor((float) short0 * 0.08F));
            }

            if (this.dragonDeathTime == 1 && !this.isSilent()) {
                int viewDistance = this.level().getCraftServer().getViewDistance() * 16;
                Iterator var4 = this.level().getServer().getPlayerList().players.iterator();

                label48:
                while (true) {
                    double deltaX;
                    double deltaZ;
                    double distanceSquared;
                    ServerPlayer player;
                    do {
                        if (!var4.hasNext()) {
                            break label48;
                        }

                        player = (ServerPlayer) var4.next();
                        deltaX = this.getX() - player.getX();
                        deltaZ = this.getZ() - player.getZ();
                        distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    } while (this.level().spigotConfig.dragonDeathSoundRadius > 0 && distanceSquared > (double) (this.level().spigotConfig.dragonDeathSoundRadius * this.level().spigotConfig.dragonDeathSoundRadius));

                    if (distanceSquared > (double) (viewDistance * viewDistance)) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.getX() + deltaX / deltaLength * (double) viewDistance;
                        double relativeZ = player.getZ() + deltaZ / deltaLength * (double) viewDistance;
                        player.connection.send(new ClientboundLevelEventPacket(1028, new BlockPos((int) relativeX, (int) this.getY(), (int) relativeZ), 0, true));
                    } else {
                        player.connection.send(new ClientboundLevelEventPacket(1028, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0, true));
                    }
                }
            }
        }
        this.setYRot(this.getYRot() + 20.0F);
        this.yBodyRot = this.getYRot();
        if (this.dragonDeathTime == 200 && this.level() instanceof ServerLevel) {
            ExperienceOrb.award((ServerLevel) this.level(), this.position(), Mth.floor((float) short0 * 0.2F));
            if (this.getDragonFight() != null) {
                this.getDragonFight().setDragonKilled(this);
            }

            this.remove(RemovalReason.KILLED);
            this.gameEvent(GameEvent.ENTITY_DIE);
        }

    }
}
