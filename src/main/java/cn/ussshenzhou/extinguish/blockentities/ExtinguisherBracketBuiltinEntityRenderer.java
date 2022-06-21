package cn.ussshenzhou.extinguish.blockentities;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.EmptyModelData;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherBracketBuiltinEntityRenderer implements BlockEntityRenderer<ExtinguisherBracketBuiltinEntity> {
    public ExtinguisherBracketBuiltinEntityRenderer(BlockEntityRendererProvider.Context pContext) {

    }

    @Override
    public void render(ExtinguisherBracketBuiltinEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemStack[] itemStacks = pBlockEntity.getItemStacks();
        Direction direction = pBlockEntity.getBlockState().getValue(BlockStateProperties.FACING);
        if (itemStacks[0] != ItemStack.EMPTY) {
            pPoseStack.pushPose();
            pPoseStack.translate(0, 9 / 16f, 0);
            switch (direction) {
                case NORTH:
                    pPoseStack.translate(11 / 16f, -0.5 / 16f, 0 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                    break;
                case SOUTH:
                    pPoseStack.translate(5 / 16f, -0.5 / 16f, 16 / 16f);
                    break;
                case EAST:
                    pPoseStack.translate(16 / 16f, -0.5 / 16f, 11 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                    break;
                case WEST:
                    pPoseStack.translate(0 / 16f, -0.5 / 16f, 5 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));
                    break;
                default:
                    break;
            }
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            BakedModel bakedModel = itemRenderer.getModel(itemStacks[0], pBlockEntity.getLevel(), null, 0);
            itemRenderer.render(itemStacks[0], ItemTransforms.TransformType.FIXED, true, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, bakedModel);
            pPoseStack.popPose();
        }
        if (itemStacks[1] != ItemStack.EMPTY) {
            pPoseStack.pushPose();
            pPoseStack.translate(0, 9 / 16f, 0);
            switch (direction) {
                case NORTH:
                    pPoseStack.translate(5 / 16f, -0.5 / 16f, 0 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                    break;
                case SOUTH:
                    pPoseStack.translate(11 / 16f, -0.5 / 16f, 16 / 16f);
                    break;
                case EAST:
                    pPoseStack.translate(16 / 16f, -0.5 / 16f, 5 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                    break;
                case WEST:
                    pPoseStack.translate(0 / 16f, -0.5 / 16f, 11 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));
                    break;
                default:
                    break;
            }
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            BakedModel bakedModel = itemRenderer.getModel(itemStacks[1], pBlockEntity.getLevel(), null, 0);
            itemRenderer.render(itemStacks[1], ItemTransforms.TransformType.FIXED, true, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, bakedModel);
            pPoseStack.popPose();
        }
        renderDisguise(pBlockEntity, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
    }

    private void renderDisguise(ExtinguisherBracketBuiltinEntity pBlockEntity, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        BakedModel bakedModel = pBlockEntity.getDisguiseModel();
        if (bakedModel!=null){
            pPoseStack.pushPose();
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                    pPoseStack.last(),
                    pBufferSource.getBuffer(RenderType.translucent()),
                    null,
                    bakedModel,
                    1, 1, 1, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
            //Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState,pPoseStack,pBufferSource,pPackedLight,pPackedOverlay);
            pPoseStack.popPose();
        }
    }
}
