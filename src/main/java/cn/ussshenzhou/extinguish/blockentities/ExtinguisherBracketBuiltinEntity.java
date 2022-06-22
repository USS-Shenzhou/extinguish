package cn.ussshenzhou.extinguish.blockentities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherBracketBuiltinEntity extends ExtinguisherBracketDoubleEntity {
    BlockState blockState = Blocks.AIR.defaultBlockState();

    public ExtinguisherBracketBuiltinEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ExtinguisherBracketBuiltinEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("disguise", NbtUtils.writeBlockState(blockState));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.put("disguise", NbtUtils.writeBlockState(blockState));
        return compoundTag;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.blockState = NbtUtils.readBlockState(pTag.getCompound("disguise"));
    }

    public void setDisguise(BlockState disguiseState) {
        blockState = disguiseState;
        /*Direction direction = level.getBlockState(this.getBlockPos()).getValue(BlockStateProperties.FACING);
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
        };*/
        sync();
    }

    public BlockState getDisguiseBlockState() {
        return blockState;
    }
}
