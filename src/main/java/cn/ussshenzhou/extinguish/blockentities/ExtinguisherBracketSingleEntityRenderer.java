package cn.ussshenzhou.extinguish.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * @author Tony Yu
 */
public class ExtinguisherBracketSingleEntityRenderer implements BlockEntityRenderer<ExtinguisherBracketSingleEntity> {
    public ExtinguisherBracketSingleEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public void render(ExtinguisherBracketSingleEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemStack itemStack = pBlockEntity.getItemStack();
        if (itemStack != ItemStack.EMPTY) {
            pPoseStack.pushPose();
            pPoseStack.translate(0, 9 / 16f, 0);
            switch (pBlockEntity.getBlockState().getValue(BlockStateProperties.FACING)) {
                case NORTH:
                    pPoseStack.translate(8 / 16f, 0, 11 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                    break;
                case SOUTH:
                    pPoseStack.translate(8 / 16f, 0, 5 / 16f);
                    break;
                case EAST:
                    pPoseStack.translate(5 / 16f, 0, 8 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                    break;
                case WEST:
                    pPoseStack.translate(11 / 16f, 0, 8 / 16f);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));
                    break;
                default:
                    break;
            }
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            BakedModel bakedModel = itemRenderer.getModel(itemStack, pBlockEntity.getLevel(), null, 0);
            itemRenderer.render(itemStack, ItemTransforms.TransformType.FIXED, true, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, bakedModel);
            pPoseStack.popPose();
        }
    }
}
