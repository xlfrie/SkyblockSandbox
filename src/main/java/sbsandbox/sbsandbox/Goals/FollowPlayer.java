package sbsandbox.sbsandbox.Goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PathfindToRaidGoal;

public class FollowPlayer extends Goal {

    private Mob followingMob;
//    public FollowPlayer(Mob insentient) {
//        insentient.goalSelector.addGoal(2, new PathfindToRaidGoal<>());
//    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public void tick() {
        if (this.followingMob == null) {

        }
    }
}
