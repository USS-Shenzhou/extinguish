package cn.ussshenzhou.extinguish.util;

import cn.ussshenzhou.extinguish.items.FireExtinguisherCo2;
import cn.ussshenzhou.extinguish.items.FireExtinguisherDry;
import cn.ussshenzhou.extinguish.items.FireExtinguisherWater;
import cn.ussshenzhou.extinguish.items.ModItemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author Tony Yu
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModItemPropertyOverride {
    @SubscribeEvent
    public static void itemPropertyOverride(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItemRegistry.FIRE_EXTINGUISHER_CO2.get(), new ResourceLocation("using"), ((pStack, pLevel, pEntity, pSeed) -> {
                return pEntity != null && pEntity.getUseItem().getItem() instanceof FireExtinguisherCo2 && pStack.getOrCreateTag().getBoolean("using") ? 1 : 0;
                //return pEntity != null && pEntity.getUseItem() == pStack ? 1 : 0;
            }));
            ItemProperties.register(ModItemRegistry.FIRE_EXTINGUISHER_DRY.get(), new ResourceLocation("using"), ((pStack, pLevel, pEntity, pSeed) -> {
                return pEntity != null && pEntity.getUseItem().getItem() instanceof FireExtinguisherDry && pStack.getOrCreateTag().getBoolean("using") ? 1 : 0;
            }));
            ItemProperties.register(ModItemRegistry.FIRE_EXTINGUISHER_WATER.get(), new ResourceLocation("using"), ((pStack, pLevel, pEntity, pSeed) -> {
                return pEntity != null && pEntity.getUseItem().getItem() instanceof FireExtinguisherWater && pStack.getOrCreateTag().getBoolean("using") ? 1 : 0;
            }));
        });
    }
}
