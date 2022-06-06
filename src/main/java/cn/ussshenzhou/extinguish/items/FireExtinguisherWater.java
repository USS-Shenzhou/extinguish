package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.particles.WaterSpoutParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class FireExtinguisherWater extends AbstractFireExtinguisher {
    public FireExtinguisherWater() {
        super(360);
    }

    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 5);
        Vec3 speedVec = getSpeedVec(player);
        if (player.level.dimension() != Level.NETHER){
            addParticle(new WaterSpoutParticleOption(), 4, player.level, nozzlePos, speedVec, 0.02f, 0.6f);
        } else {
            //TODO water in nether
            addParticle(ParticleTypes.LARGE_SMOKE,1, player.level, nozzlePos, speedVec, 0.06f, 0.1f);
        }
    }
}
