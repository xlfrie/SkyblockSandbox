package sbsandbox.sbsandbox.mobs;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.event.entity.EntityTargetEvent;

import static sbsandbox.sbsandbox.SBSandbox.collision;

public class CustomGuardian extends Guardian {
    public CustomGuardian(Location loc, LivingEntity e) {
        super(EntityType.GUARDIAN, ((CraftWorld) loc.getWorld()).getHandle());
        this.setTarget(e, EntityTargetEvent.TargetReason.CUSTOM, false);
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        this.setInvisible(true);
        this.setInvulnerable(true);
        collision.addEntry(this.getStringUUID());
    }

    public void registerGoals() {
        this.collides = false;
        this.setNoGravity(true);
        this.setInvisible(true);
        this.moveControl = null;
        return;
    }

    public boolean isAttackable() {
        return false;
    }

    public boolean skipAttackInteraction(Entity entity) {
        return true;
    }

    protected void updateInvisibilityStatus() {
        this.setInvisible(true);
    }

}
