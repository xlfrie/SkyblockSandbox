package me.xlfrie.sbsandbox.Mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import org.bukkit.Location;

public class Kraken extends WitherBoss {
    public static final double DEFAULT_BASE_GRAVITY = 0;
    public Kraken(EntityType<? extends WitherBoss> entitytypes, Level world, Location location) {
        super(entitytypes, world);
        this.setInvulnerableTicks(10000);
        this.bossEvent.removeAllPlayers();
        this.setNoGravity(true);
        this.setPos(location.getX(),location.getY()+15,location.getZ());
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.setInvulnerableTicks(10_000);
    }


}
