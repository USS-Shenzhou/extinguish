package ussshenzhou.extinguish.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import ussshenzhou.extinguish.particles.DryPowderParticleOption;

/**
 * @author Tony Yu
 */
public class FireExtinguisherDry extends AbstractFireExtinguisher {

    public FireExtinguisherDry() {
        super(360);
    }

    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 13);
        Vec3 speedVec = getSpeedVec(player);
        addParticle(new DryPowderParticleOption(), 4, player.level, nozzlePos, speedVec, 0.065f, 0.7f);
    }
}
