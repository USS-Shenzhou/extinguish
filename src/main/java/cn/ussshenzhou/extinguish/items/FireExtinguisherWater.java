package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.particles.WaterSpoutParticleOption;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class FireExtinguisherWater extends AbstractFireExtinguisher {
    public FireExtinguisherWater() {
        super(360);
    }

    //TODO:water particle
    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 5);
        Vec3 speedVec = getSpeedVec(player);
        addParticle(new WaterSpoutParticleOption(), 4, player.level, nozzlePos, speedVec, 0.03f, 0.6f);
    }
}
