package ussshenzhou.extinguish.particles;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author Tony Yu
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticleProviderRegistry {
    @SubscribeEvent
    public static void onParticleProviderRegistry(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticleRegistry.CO2_SMOKE_PARTICLE.get(), Co2SmokeParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ModParticleRegistry.DRY_POWDER_PARTICLE.get(), DryPowderParticle.Provider::new);
    }
}
