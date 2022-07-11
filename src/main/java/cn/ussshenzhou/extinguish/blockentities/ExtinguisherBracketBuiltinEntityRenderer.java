package cn.ussshenzhou.extinguish.blockentities;

import cn.ussshenzhou.extinguish.render.RawQuad;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

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
        if (pBlockEntity.getDisguiseModel() != null) {
            pPoseStack.pushPose();
            try {
                Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateWithAO(pBlockEntity.getLevel(),
                        pBlockEntity.getDisguiseModel(),
                        pBlockEntity.getDisguiseBlockState(),
                        pBlockEntity.getBlockPos(),
                        pPoseStack,
                        pBufferSource.getBuffer(RenderType.translucent()),
                        true,
                        new Random(),
                        42,
                        pPackedOverlay,
                        EmptyModelData.INSTANCE
                );
            } catch (NoSuchMethodError e) {
                Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                        pPoseStack.last(),
                        pBufferSource.getBuffer(RenderType.translucent()),
                        pBlockEntity.getDisguiseBlockState(),
                        pBlockEntity.getDisguiseModel(),
                        1, 1, 1, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE
                );
            }
            pPoseStack.popPose();
        }
    }
}
