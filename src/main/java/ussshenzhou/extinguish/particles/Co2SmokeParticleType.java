package ussshenzhou.extinguish.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class Co2SmokeParticleType extends ParticleType<Co2SmokeParticleOption> {
    public Co2SmokeParticleType() {
        super(false, Co2SmokeParticleOption.DESERIALIZER);
    }

    @Override
    public Codec<Co2SmokeParticleOption> codec() {
        return Codec.unit(new Co2SmokeParticleOption(new Vec3(0, 0, 0), new Vec3(0, 0, 0)));
    }
}
