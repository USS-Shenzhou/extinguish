package cn.ussshenzhou.extinguish.network;

import cn.ussshenzhou.extinguish.Extinguish;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * @author USS_Shenzhou
 */
public class PutOutFirePackSend {
    public static SimpleChannel channel;

    public static String VERSION = "1.0";
    private static int id = 0;

    public static int nextId() {
        return id++;
    }

    public static void registerMessage() {
        channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Extinguish.MOD_ID, "put_out_fire_pack"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        channel.messageBuilder(PutOutFirePack.class, nextId())
                .encoder(PutOutFirePack::toBytes)
                .decoder(PutOutFirePack::new)
                .consumer(PutOutFirePack::handler)
                .add();
    }
}
