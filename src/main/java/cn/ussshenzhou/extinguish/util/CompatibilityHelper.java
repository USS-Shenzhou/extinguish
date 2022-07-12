package cn.ussshenzhou.extinguish.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;

import java.util.Random;

/**
 * @author USS_Shenzhou
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CompatibilityHelper {
    /**
     * NoSuchMethodError will be throw if Optifine is installed.
     * <br/>
     * fixed: use tesselateWithAO() with out IModelData
     *
     * @see net.minecraft.client.renderer.block.ModelBlockRenderer#tesselateWithAO(BlockAndTintGetter, BakedModel, BlockState, BlockPos, PoseStack, VertexConsumer, boolean, Random, long, int)
     * @see net.minecraft.client.renderer.block.ModelBlockRenderer#tesselateWithAO(BlockAndTintGetter, BakedModel, BlockState, BlockPos, PoseStack, VertexConsumer, boolean, Random, long, int, IModelData)
     */
    @SubscribeEvent
    public static void optifine(FMLClientSetupEvent event) {
        /*try {
            Class c = Class.forName("optifine.OptiFineClassTransformer");
            IModInfo iModInfo = ModList.get().getModContainerById("extinguish")
                    .map(ModContainer::getModInfo)
                    .orElseThrow(() -> new IllegalStateException("ModInfo absent while mod itself is present"));
            ModLoader.get().addWarning(new ModLoadingWarning(
                    iModInfo,
                    ModLoadingStage.SIDED_SETUP,
                    "warn.extinguish.optifine"
            ));
        } catch (ClassNotFoundException ignored) {
        }*/
    }

    @SubscribeEvent
    public static void particle(FMLClientSetupEvent event) {
        if (Minecraft.getInstance().options.particles == ParticleStatus.MINIMAL) {
            IModInfo iModInfo = ModList.get().getModContainerById("extinguish")
                    .map(ModContainer::getModInfo)
                    .orElseThrow(() -> new IllegalStateException("ModInfo absent while mod itself is present"));

            ModLoader.get().addWarning(new ModLoadingWarning(
                    iModInfo,
                    ModLoadingStage.SIDED_SETUP,
                    "warn.extinguish.particle"
            ));
        }
    }
}
