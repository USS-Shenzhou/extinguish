package cn.ussshenzhou.extinguish.network;

import cn.ussshenzhou.extinguish.Extinguish;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * @author USS_Shenzhou
 */
public class PreciseParticlePackSend {
    public static SimpleChannel channel;

    public static String VERSION = "1.0";
    private static int id = 0;

    public static int nextId() {
        return id++;
    }

    public static void registerMessage() {
        channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Extinguish.MOD_ID, "precise_particle_pack"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        channel.messageBuilder(PreciseParticlePack.class, nextId())
                .encoder(PreciseParticlePack::write)
                .decoder(PreciseParticlePack::new)
                .consumer(PreciseParticlePack::handler)
                .add();
    }
}
