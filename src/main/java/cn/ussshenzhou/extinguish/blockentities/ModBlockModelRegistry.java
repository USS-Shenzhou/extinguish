package cn.ussshenzhou.extinguish.blockentities;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author USS_Shenzhou
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBlockModelRegistry {
    @SubscribeEvent
    public static void onRegisterLayerDef(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(new ModelLayerLocation(AutoWaterCannonEntityModel.LOCATION, "main"), AutoWaterCannonEntityModel::createBodyLayer);
        event.registerLayerDefinition(new ModelLayerLocation(AutoWaterCannonEntityModel.LOCATION_WHITE, "main"), AutoWaterCannonEntityModel::createBodyLayer);
    }
}
