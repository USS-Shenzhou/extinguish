package ussshenzhou.extinguish.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class DrySmokeParticleType extends ParticleType<DrySmokeParticleOption> {
    public DrySmokeParticleType() {
        super(false,DrySmokeParticleOption.DESERIALIZER);
    }

    @Override
    public Codec<DrySmokeParticleOption> codec() {
        return Codec.unit(new DrySmokeParticleOption(new Vec3(0, 0, 0), new Vec3(0, 0, 0)));

    }
}
