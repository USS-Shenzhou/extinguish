package cn.ussshenzhou.extinguish.network;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * @author USS_Shenzhou
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModNetworkRegister {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PutOutFirePackSend.registerMessage();
            PreciseParticlePackSend.registerMessage();
            ExtinguisherSoundPackSend.registerMessage();
        });
    }
}
