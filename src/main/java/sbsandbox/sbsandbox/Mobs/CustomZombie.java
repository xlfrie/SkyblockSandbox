package sbsandbox.sbsandbox.Mobs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

import java.util.UUID;

public class CustomZombie extends Villager {
    public CustomZombie(Location loc) {
        super(EntityType.VILLAGER, ((CraftWorld) loc.getWorld()).getHandle());

        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        this.setCustomName(Component.literal(ChatColor.RED + "Stinky"));
        this.setSpeed(1000);
        this.goalSelector.removeAllGoals();
        this.targetSelector.removeAllGoals();

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 4, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Zombie.class, true));
        this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", 2, AttributeModifier.Operation.ADDITION));
    }

//    @Override
//    protected boolean isSunSensitive() {
//        return false;
//    }



    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 48.0);
    }
}
