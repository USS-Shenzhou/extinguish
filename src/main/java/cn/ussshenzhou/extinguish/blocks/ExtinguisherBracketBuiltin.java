package cn.ussshenzhou.extinguish.blocks;

import cn.ussshenzhou.extinguish.blockentities.ExtinguisherBracketBuiltinEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherBracketBuiltin extends AbstractExtinguisherBracket {

    protected static BooleanProperty open = BlockStateProperties.OPEN;
    private static final VoxelShape BASE = box(0.01, 0.01, 0.01, 15.99, 15.99, 15.99);

    public ExtinguisherBracketBuiltin() {
        super();
        this.registerDefaultState(this.getStateDefinition().any().setValue(direction, Direction.NORTH).setValue(open, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BASE;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pHit.getDirection() != pState.getValue(direction)) {
            return InteractionResult.PASS;
        }

        if (!pLevel.isClientSide) {
            if (pState.getValue(open)) {
                if (pPlayer.isShiftKeyDown()) {
                    //TODO sound
                    pLevel.setBlock(pPos, pState.setValue(open, false), 2);
                } else {
                    return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
                }
            } else {
                pLevel.setBlock(pPos, pState.setValue(open, true), 2);
            }
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * called when hit in creative to close the door
     */
    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        LogManager.getLogger().debug("111");
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    /**
     * called when hit in survival to close the door
     */
    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        LogManager.getLogger().debug("111");
        super.attack(pState, pLevel, pPos, pPlayer);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(open);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ExtinguisherBracketBuiltinEntity(pPos, pState);
    }
}
