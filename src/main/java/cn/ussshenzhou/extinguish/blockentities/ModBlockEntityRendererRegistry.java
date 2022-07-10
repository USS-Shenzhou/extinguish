package cn.ussshenzhou.extinguish.blockentities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author USS_Shenzhou
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBlockEntityRendererRegistry {
    @SubscribeEvent
    public static void bind(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_SINGLE_BLOCK_ENTITY.get(), ExtinguisherBracketSingleEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_DOUBLE_BLOCK_ENTITY.get(), ExtinguisherBracketDoubleEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY.get(), ExtinguisherBracketBuiltinEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY.get(), pContext -> new AutoWaterCannonEntityRenderer(pContext, AutoWaterCannonEntityModel.LOCATION));
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY_WHITE.get(),pContext -> new AutoWaterCannonEntityRenderer(pContext, AutoWaterCannonEntityModel.LOCATION_WHITE));
    }
}
