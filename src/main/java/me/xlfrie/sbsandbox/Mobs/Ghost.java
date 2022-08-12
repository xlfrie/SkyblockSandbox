package me.xlfrie.sbsandbox.Mobs;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;

public class Ghost extends Creeper {

    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR;
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED;
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED;

    static {
        DATA_SWELL_DIR = SynchedEntityData.defineId(Ghost.class, EntityDataSerializers.INT);
        DATA_IS_POWERED = SynchedEntityData.defineId(Ghost.class, EntityDataSerializers.BOOLEAN);
        DATA_IS_IGNITED = SynchedEntityData.defineId(Ghost.class, EntityDataSerializers.BOOLEAN);
    }

    public Ghost(EntityType<? extends Creeper> entitytypes, Level world, Location location) {
        super(entitytypes, world);
        this.setPos(location.getX(), location.getY(), location.getZ());
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
        NearestAttackableTargetGoal<Player> natg = new NearestAttackableTargetGoal<>(this, Player.class, true);
        this.targetSelector.addGoal(1, natg);
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
        this.setInvisible(true);
        this.setPowered(true);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
        this.entityData.define(DATA_IS_POWERED, true);
        this.entityData.define(DATA_IS_IGNITED, false);
    }

    protected void updateInvisibilityStatus() {
        this.setInvisible(true);
    }

    @Override
    public void knockback(double d0, double d1, double d2) {
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }
}
