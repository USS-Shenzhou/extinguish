package cn.ussshenzhou.extinguish.network;

import cn.ussshenzhou.extinguish.Extinguish;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherSoundPackSend {
    public static SimpleChannel channel;

    public static String VERSION = "1.0";
    private static int id = 0;

    public static int nextId() {
        return id++;
    }

    public static void registerMessage() {
        channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Extinguish.MOD_ID, "extinguisher_sound_pack"),
                () -> VERSION,
                (v) -> v.equals(VERSION),
                (v) -> v.equals(VERSION)
        );
        channel.messageBuilder(ExtinguisherSoundPack.class, nextId())
                .encoder(ExtinguisherSoundPack::write)
                .decoder(ExtinguisherSoundPack::new)
                .consumer(ExtinguisherSoundPack::handler)
                .add();
    }
}
