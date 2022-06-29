package cn.ussshenzhou.extinguish.blocks;

import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntity;
import cn.ussshenzhou.extinguish.blockentities.ModBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * @author USS_Shenzhou
 */
public class AutoWaterCannon extends BaseEntityBlock {
    private static final VoxelShape DOWN = box(4,7,4,12,16,12);
    private static final VoxelShape UP = box(4,0,4,12,9,12);
    protected AutoWaterCannon() {
        super(
                Properties.of(Material.METAL)
                        .noOcclusion()
                        .strength(1,5)
        );
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(BlockStateProperties.FACING);
        return switch (direction) {
            case DOWN -> DOWN;
            case UP -> UP;
            default -> DOWN;
        };
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ?
                createTickerHelper(pBlockEntityType, ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY.get(), AutoWaterCannonEntity::clientTick):
                createTickerHelper(pBlockEntityType, ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY.get(), AutoWaterCannonEntity::serverTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AutoWaterCannonEntity(pPos,pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(BlockStateProperties.FACING);
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, pContext.getNearestLookingVerticalDirection().getOpposite());
    }
}
