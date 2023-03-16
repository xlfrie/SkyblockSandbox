package me.xlfrie.sbsandbox.Mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import org.bukkit.Location;

public class LeChonk extends Pig {
    public LeChonk(EntityType<? extends Pig> entitytypes, Level world, Location loc) {
        super(entitytypes, world);
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
    }
}
