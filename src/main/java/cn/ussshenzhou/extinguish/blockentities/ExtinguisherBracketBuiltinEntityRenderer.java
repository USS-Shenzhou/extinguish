package cn.ussshenzhou.extinguish.blockentities;

import cn.ussshenzhou.extinguish.render.RawQuad;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        renderShell(pBlockEntity, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
    }

    private void renderShell(ExtinguisherBracketBuiltinEntity pBlockEntity, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
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
