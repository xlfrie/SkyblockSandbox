package me.xlfrie.sbsandbox.Dungeons.F6.mobs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

import java.util.EnumSet;

public class WokeGolem extends IronGolem {
    public WokeGolem(Location loc, Component name) {
        super(EntityType.IRON_GOLEM, ((CraftWorld) loc.getWorld()).getHandle());

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.7);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5);
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        this.setCustomName(name);
        this.goalSelector.getAvailableGoals().forEach((goal) -> this.goalSelector.removeGoal(goal));
        this.targetSelector.getAvailableGoals().forEach((goal) -> this.targetSelector.removeGoal(goal));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.goalSelector.addGoal(1, new GolemAttack(this, this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue(), true));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(48);
        this.setSprinting(true);
        this.setCustomNameVisible(true);
        ((CraftWorld) loc.getWorld()).getHandle().addFreshEntity(this);
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    public class GolemAttack extends Goal {
        private final PathfinderMob mob;
        private int ticksUntilNextAttack;
        private int ticksUntilReCalc;
        private PathNavigation pathNav;
        private int ticksOnGround = -1;

        public GolemAttack(PathfinderMob var0, double var1, boolean var3) {
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
            if (this.mob.onGround) {
                this.ticksOnGround++;
                if (this.ticksOnGround >= 0) {
                    this.mob.getJumpControl().jump();
                    this.ticksOnGround = -1;
                }
            }
            if (ticksUntilReCalc <= 0) {
                this.mob.getLookControl().setLookAt(this.mob.getTarget());
                this.pathNav = this.mob.getNavigation();
                this.ticksUntilReCalc = -20;
            }
            this.pathNav.moveTo(this.mob.getTarget(), 1);
            if (!this.mob.isSprinting()) this.mob.setSprinting(true);
            this.ticksUntilNextAttack++;
            this.ticksUntilReCalc++;
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
            return (double) (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + var0.getBbWidth());
        }
    }
}
