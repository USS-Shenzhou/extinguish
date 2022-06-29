package cn.ussshenzhou.extinguish.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class WaterSpoutParticleType extends ParticleType<WaterSpoutParticleOption> {
    public WaterSpoutParticleType() {
        super(false, WaterSpoutParticleOption.DESERIALIZER);
    }

    @Override
    public Codec<WaterSpoutParticleOption> codec() {
        return Codec.unit(new WaterSpoutParticleOption(null,new Vec3(0, 0, 0), new Vec3(0, 0, 0)));
    }
}
