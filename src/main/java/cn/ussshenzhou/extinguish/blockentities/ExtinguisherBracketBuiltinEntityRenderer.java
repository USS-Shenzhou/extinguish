package cn.ussshenzhou.extinguish.blockentities;

import cn.ussshenzhou.extinguish.render.RawQuad;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
        Direction direction = pBlockEntity.getBlockState().getValue(BlockStateProperties.FACING);
        BlockState blockState = pBlockEntity.getDisguiseBlockState();
        BakedModel blockModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
        //TODO 持久化
        List<BakedQuad> quadList = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (d == direction) {
                continue;
            }
            Random r = new Random(42L);
            quadList.addAll(blockModel.getQuads(blockState, d, r, EmptyModelData.INSTANCE));
        }
        List<BakedQuad> l = blockModel.getQuads(blockState, direction, new Random(), EmptyModelData.INSTANCE);
        for (BakedQuad b : l) {
            RawQuad rawQuad = new RawQuad(b);
            quadList.add(rawQuad.shrink(0, 15, 0, 0).bake());
            quadList.add(rawQuad.shrink(15, 0, 0, 0).bake());
            quadList.add(rawQuad.shrink(1, 1, 14, 0).bake());
            quadList.add(rawQuad.shrink(1, 1, 0, 14).bake());

        }
        BakedModel disguiseModel = new BakedModel() {
            @NotNull
            @Override
            public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
                return quadList;
            }

            @Override
            public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, Random pRand) {
                return quadList;
            }

            @Override
            public boolean useAmbientOcclusion() {
                return blockModel.useAmbientOcclusion();
            }

            @Override
            public boolean isGui3d() {
                return blockModel.isGui3d();
            }

            @Override
            public boolean usesBlockLight() {
                return blockModel.usesBlockLight();
            }

            @Override
            public boolean isCustomRenderer() {
                return blockModel.isCustomRenderer();
            }

            @Override
            public TextureAtlasSprite getParticleIcon() {
                return blockModel.getParticleIcon();
            }

            @Override
            public ItemOverrides getOverrides() {
                return blockModel.getOverrides();
            }
        };
        //BakedModel bakedModel = pBlockEntity.getDisguiseModel();
        //if (bakedModel != null) {
        pPoseStack.pushPose();
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                pPoseStack.last(),
                pBufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(pBlockEntity.getDisguiseBlockState(), false)),
                null,
                disguiseModel,
                1, 1, 1, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        pPoseStack.popPose();
        //}
    }
}
