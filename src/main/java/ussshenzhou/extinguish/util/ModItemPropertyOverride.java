package ussshenzhou.extinguish.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ussshenzhou.extinguish.items.AbstractFireExtinguisher2;
import ussshenzhou.extinguish.items.ModItemRegistry;

/**
 * @author Tony Yu
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModItemPropertyOverride {
    @SubscribeEvent
    public static void itemPropertyOverride(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItemRegistry.FIRE_EXTINGUISHER_CO2.get(), new ResourceLocation("using"), ((pStack, pLevel, pEntity, pSeed) -> {
                return pEntity != null && pEntity.getUseItem().getItem() instanceof AbstractFireExtinguisher2 && pStack.getOrCreateTag().getBoolean("using") ? 1 : 0;
            }));
        });
    }
}
