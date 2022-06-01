package ussshenzhou.extinguish.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ussshenzhou.extinguish.blockentities.ExtinguisherBracketSingleEntity;
import ussshenzhou.extinguish.items.AbstractFireExtinguisher;

/**
 * @author Tony Yu
 */
public class ExtinguisherBracketSingle extends AbstractExtinguisherBracket {
    private static final VoxelShape NORTH = Block.box(6, 0, 11.5, 10, 10, 16);
    private static final VoxelShape SOUTH = Block.box(6, 0, 0, 10, 10, 4.5);
    private static final VoxelShape WEST = Block.box(11.5, 0, 6, 16, 10, 10);
    private static final VoxelShape EAST = Block.box(0, 0, 6, 4.5, 10, 10);

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
        return new ExtinguisherBracketSingleEntity(pPos, pState);
    }

}
