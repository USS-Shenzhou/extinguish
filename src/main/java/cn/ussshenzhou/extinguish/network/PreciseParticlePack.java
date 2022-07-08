package cn.ussshenzhou.extinguish.network;

import cn.ussshenzhou.extinguish.fire.FireHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

/**
 * @author USS_Shenzhou
 * @see net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket
 */
public class PreciseParticlePack {
    private ParticleOptions particleOption;
    private double x;
    private double y;
    private double z;
    private double posDiffuse;
    private double speedX;
    private double speedY;
    private double speedZ;
    private double speedDiffuse;
    private int count;

    public PreciseParticlePack(ParticleOptions particleOption, double x, double y, double z, double posDiffuse, double speedX, double speedY, double speedZ, double speedDiffuse, int count) {
        this.particleOption = particleOption;
        this.x = x;
        this.y = y;
        this.z = z;
        this.posDiffuse = posDiffuse;
        this.speedX = speedX;
        this.speedY = speedY;
        this.speedZ = speedZ;
        this.speedDiffuse = speedDiffuse;
        this.count = count;
    }

    public PreciseParticlePack(FriendlyByteBuf buffer) {
        ParticleType<?> particleType = Registry.PARTICLE_TYPE.byId(buffer.readInt());
        this.particleOption = readParticle(buffer, particleType);
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.posDiffuse = buffer.readDouble();
        this.speedX = buffer.readDouble();
        this.speedY = buffer.readDouble();
        this.speedZ = buffer.readDouble();
        this.speedDiffuse = buffer.readDouble();
        this.count = buffer.readInt();
    }

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
        return pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(Registry.PARTICLE_TYPE.getId(particleOption.getType()));
        particleOption.writeToNetwork(buffer);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
        buffer.writeDouble(this.posDiffuse);
        buffer.writeDouble(this.speedX);
        buffer.writeDouble(this.speedY);
        buffer.writeDouble(this.speedZ);
        buffer.writeDouble(this.speedDiffuse);
        buffer.writeInt(this.count);
    }

    public void handler(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(
                () -> {
                    if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {

                    } else {
                        Level level = Minecraft.getInstance().level;
                        Random r = new Random();
                        for (int i = 0; i < count; i++) {
                            level.addParticle(
                                    particleOption,
                                    false,
                                    x + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * posDiffuse,
                                    y + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * posDiffuse,
                                    z + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * posDiffuse,
                                    speedX + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * speedDiffuse,
                                    speedY + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * speedDiffuse,
                                    speedZ + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * speedDiffuse
                            );
                        }
                    }
                }
        );
        context.get().setPacketHandled(true);
    }
}
