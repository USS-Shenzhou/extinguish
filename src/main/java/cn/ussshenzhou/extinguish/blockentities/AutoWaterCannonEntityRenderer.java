package cn.ussshenzhou.extinguish.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * @author USS_Shenzhou
 */
public class AutoWaterCannonEntityRenderer implements BlockEntityRenderer<AutoWaterCannonEntity> {
    private final AutoWaterCannonEntityModel autoWaterCannonEntityModel;
    private final Material material;
    private static final float PI = (float) Math.PI;

    public AutoWaterCannonEntityRenderer(BlockEntityRendererProvider.Context pContext, ResourceLocation location) {
        autoWaterCannonEntityModel = new AutoWaterCannonEntityModel(pContext.bakeLayer(new ModelLayerLocation(location, "main")));
        material = new Material(TextureAtlas.LOCATION_BLOCKS, location);
    }

    @Override
    public void render(AutoWaterCannonEntity blockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
        VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityCutoutNoCull);
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        switch (direction) {
            case DOWN:
                autoWaterCannonEntityModel.getBase().setRotation(0, Mth.lerp(pPartialTick, blockEntity.getPrevYaw(), blockEntity.getYaw()), 0);
                autoWaterCannonEntityModel.getTube().setRotation(PI - Mth.lerp(pPartialTick, blockEntity.getPrevPitch(), blockEntity.getPitch()), 0, 0);
                autoWaterCannonEntityModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
                break;
            case UP:
                break;
            default:
                break;
        }
        poseStack.popPose();
    }
}
