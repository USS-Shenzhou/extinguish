package cn.ussshenzhou.extinguish.blockentities;

import cn.ussshenzhou.extinguish.blocks.AbstractExtinguisherBracket;
import cn.ussshenzhou.extinguish.render.RawQuad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherBracketBuiltinEntity extends ExtinguisherBracketDoubleEntity {

    public ExtinguisherBracketBuiltinEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ExtinguisherBracketBuiltinEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    //TODO disguiseModel持久化
    private BakedModel disguiseModel = null;

    public void setDisguise(BlockItem blockItem, Level level, BlockPos pos) {
        Block block = blockItem.getBlock();
        BlockState blockState = block.defaultBlockState();
        if (blockState.getShape(level, pos) == Shapes.block()) {
            Direction direction = level.getBlockState(pos).getValue(BlockStateProperties.FACING);
            try {
                blockState = blockState.setValue(BlockStateProperties.FACING, direction);
            } catch (IllegalArgumentException ignored) {
            }
            BakedModel blockModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
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
            disguiseModel = new BakedModel() {
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
        }
    }

    public BakedModel getDisguiseModel() {
        return disguiseModel;
    }
}
