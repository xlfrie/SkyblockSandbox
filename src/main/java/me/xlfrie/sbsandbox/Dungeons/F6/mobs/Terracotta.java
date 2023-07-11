package me.xlfrie.sbsandbox.Dungeons.F6.mobs;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

import java.util.EnumSet;

public class Terracotta extends Zombie {
    public Terracotta(Location loc, Component name) {
        super(EntityType.ZOMBIE, ((CraftWorld) loc.getWorld()).getHandle());

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.65);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5);
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        this.setCustomName(name);
        this.goalSelector.getAvailableGoals().forEach((goal) -> this.goalSelector.removeGoal(goal));
        this.targetSelector.getAvailableGoals().forEach((goal) -> this.targetSelector.removeGoal(goal));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.goalSelector.addGoal(1, new TerracottaAttack(this, this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue(), true));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(48);
        this.setSprinting(true);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.STONE_BREAK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.PLAYER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.GRASS_STEP;
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    public class TerracottaAttack extends Goal {
        private final PathfinderMob mob;
        private int ticksUntilNextAttack;

        public TerracottaAttack(PathfinderMob var0, double var1, boolean var3) {
            this.mob = var0;
            this.ticksUntilNextAttack = 0;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            if (this.mob.getTarget() == null) return;
            this.checkAndPerformAttack(this.mob.getTarget(), this.mob.distanceToSqr(this.mob.getTarget().getX(), this.mob.getTarget().getY(), this.mob.getTarget().getZ()));
            if (this.mob.onGround && this.mob.distanceToSqr(this.mob.getTarget().getX(), this.mob.getTarget().getY(), this.mob.getTarget().getZ()) >= 7) {
                this.mob.getJumpControl().jump();
                this.mob.getNavigation().moveTo(this.mob.getTarget(), this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue());
            } else if (this.mob.distanceToSqr(this.mob.getTarget().getX(), this.mob.getTarget().getY(), this.mob.getTarget().getZ()) < 7)
                this.mob.getNavigation().moveTo(this.mob.getTarget(), this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() * 0.75);
            if (!this.mob.isSprinting()) this.mob.setSprinting(true);
            this.mob.getLookControl().setLookAt(this.mob.getTarget());
            this.ticksUntilNextAttack++;
        }

        protected void checkAndPerformAttack(LivingEntity var0, double var1) {
            double var3 = this.getAttackReachSqr(var0);
            if (var1 <= var3 && this.ticksUntilNextAttack >= 0) {
                this.ticksUntilNextAttack = -5;
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(var0);
            }
        }

        protected double getAttackReachSqr(LivingEntity var0) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + var0.getBbWidth());
        }
    }
}
