package cn.ussshenzhou.extinguish.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class DryPowderParticleOption implements ParticleOptions {
    private final Vec3 pos;
    private final Vec3 speed;
    public static final ParticleOptions.Deserializer<DryPowderParticleOption> DESERIALIZER = new Deserializer<DryPowderParticleOption>() {
        @Override
        public DryPowderParticleOption fromCommand(ParticleType<DryPowderParticleOption> pParticleType, StringReader pReader) throws CommandSyntaxException {
            pReader.expect(' ');
            double f = pReader.readDouble();
            pReader.expect(' ');
            double f1 = pReader.readDouble();
            pReader.expect(' ');
            double f2 = pReader.readDouble();
            pReader.expect(' ');
            double f3 = pReader.readDouble();
            pReader.expect(' ');
            double f4 = pReader.readDouble();
            pReader.expect(' ');
            double f5 = pReader.readDouble();
            return new DryPowderParticleOption(new Vec3(f, f1, f2), new Vec3(f3, f4, f5));
        }

        @Override
        public DryPowderParticleOption fromNetwork(ParticleType<DryPowderParticleOption> pParticleType, FriendlyByteBuf pBuffer) {
            double f = pBuffer.readDouble();
            double f1 = pBuffer.readDouble();
            double f2 = pBuffer.readDouble();
            double f3 = pBuffer.readDouble();
            double f4 = pBuffer.readDouble();
            double f5 = pBuffer.readDouble();
            return new DryPowderParticleOption(new Vec3(f, f1, f2), new Vec3(f3, f4, f5));
        }
    };
    public DryPowderParticleOption(Vec3 pos, Vec3 speed) {
        this.pos = pos;
        this.speed = speed;
    }

    public DryPowderParticleOption() {
        this.pos = new Vec3(0, 0, 0);
        this.speed = new Vec3(0, 0, 0);
    }
    @Override
    public ParticleType<?> getType() {
        return ModParticleRegistry.DRY_POWDER_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeDouble(pos.x);
        pBuffer.writeDouble(pos.y);
        pBuffer.writeDouble(pos.z);
        pBuffer.writeDouble(speed.x);
        pBuffer.writeDouble(speed.y);
        pBuffer.writeDouble(speed.z);
    }

    @Override
    public String writeToString() {
        return pos + " " + speed;
    }
    public Vec3 getPos() {
        return pos;
    }

    public Vec3 getSpeed() {
        return speed;
    }
}