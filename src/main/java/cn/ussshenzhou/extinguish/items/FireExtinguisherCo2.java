package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.particles.Co2SmokeParticleOption;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class FireExtinguisherCo2 extends AbstractFireExtinguisher {
    public FireExtinguisherCo2() {
        super(15 * 20);
    }

    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 10, 3.25f);
        Vec3 speedVec = getSpeedVec(player);
        addParticle(new Co2SmokeParticleOption(), 3, player.level, nozzlePos, speedVec, 0.08f, 0.6f);
    }
}
