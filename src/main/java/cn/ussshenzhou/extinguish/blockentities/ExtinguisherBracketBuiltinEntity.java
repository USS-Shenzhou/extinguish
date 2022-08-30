package cn.ussshenzhou.extinguish.blockentities;

import cn.ussshenzhou.t88.render.RawQuad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherBracketBuiltinEntity extends ExtinguisherBracketDoubleEntity {
    private BlockState disguiseBlockState = Blocks.AIR.defaultBlockState();
    private BakedModel disguiseModel;

    public ExtinguisherBracketBuiltinEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ExtinguisherBracketBuiltinEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("disguise", NbtUtils.writeBlockState(disguiseBlockState));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.put("disguise", NbtUtils.writeBlockState(disguiseBlockState));
        return compoundTag;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.disguiseBlockState = NbtUtils.readBlockState(pTag.getCompound("disguise"));
        if (level != null && level.isClientSide) {
            setDisguise(disguiseBlockState);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ExtinguisherBracketBuiltinEntity thisEntity) {
        thisEntity.syncTick();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            syncFromServer(level,this);
        }
    }
    boolean needSync = true;

    void syncTick() {
        if (needSync) {
            syncFromServer(level, this);
            needSync = false;
        }
    }

    public void setDisguise(BlockState disguiseState) {
        disguiseBlockState = disguiseState;
        if (level.isClientSide) {
            calculateDisguiseModel();
        }
        syncFromServer(level, this);
    }

    @OnlyIn(Dist.CLIENT)
    public void calculateDisguiseModel() {
        Direction direction = this.getBlockState().getValue(BlockStateProperties.FACING);
        BakedModel blockModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(disguiseBlockState);
        List<BakedQuad> quadList = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (d == direction) {
                continue;
            }
            Random r = new Random(42L);
            quadList.addAll(blockModel.getQuads(disguiseBlockState, d, r, EmptyModelData.INSTANCE));
        }
        List<BakedQuad> l = blockModel.getQuads(disguiseBlockState, direction, new Random(), EmptyModelData.INSTANCE);
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

    @OnlyIn(Dist.CLIENT)
    public BakedModel getDisguiseModel() {
        return disguiseModel;
    }

    public BlockState getDisguiseBlockState() {
        return disguiseBlockState;
    }

}
