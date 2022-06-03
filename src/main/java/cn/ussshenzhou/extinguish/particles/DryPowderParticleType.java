package cn.ussshenzhou.extinguish.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class DryPowderParticleType extends ParticleType<DryPowderParticleOption> {
    public DryPowderParticleType() {
        super(false, DryPowderParticleOption.DESERIALIZER);
    }

    @Override
    public Codec<DryPowderParticleOption> codec() {
        return Codec.unit(new DryPowderParticleOption(new Vec3(0, 0, 0), new Vec3(0, 0, 0)));

    }
}
