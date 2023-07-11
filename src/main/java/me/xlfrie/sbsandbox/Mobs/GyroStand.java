package me.xlfrie.sbsandbox.Mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class GyroStand extends ArmorStand {
    public GyroStand(EntityType<? extends ArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
    }

    public GyroStand(Level world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 15) {
            Objects.requireNonNull(this.getFirstPassenger()).kill();
            this.kill();
        }
    }
}
