package cn.ussshenzhou.extinguish.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import cn.ussshenzhou.extinguish.blockentities.ExtinguisherBracketDoubleEntity;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherBracketDouble extends AbstractExtinguisherBracket {
    private static final VoxelShape NORTH = box(2, 0, 11.5, 14, 12, 16);
    private static final VoxelShape SOUTH = box(2, 0, 0, 14, 12, 4.5);
    private static final VoxelShape WEST = box(11.5, 0, 2, 16, 12, 14);
    private static final VoxelShape EAST = box(0, 0, 2, 4.5, 12, 14);


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction d = pState.getValue(direction);
        return switch (d) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> NORTH;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ExtinguisherBracketDoubleEntity(pPos, pState);
    }
}
