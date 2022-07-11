package cn.ussshenzhou.extinguish.blocks;

import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntity;
import cn.ussshenzhou.extinguish.blockentities.ExtinguisherBracketBuiltinEntity;
import cn.ussshenzhou.extinguish.blockentities.ModBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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

    /*@Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BASE;
    }*/

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pHit.getDirection() != pState.getValue(direction)) {
            //hit other sides
            ExtinguisherBracketBuiltinEntity extinguisherBracketBuiltinEntity = (ExtinguisherBracketBuiltinEntity) pLevel.getBlockEntity(pPos);
            Item item = pPlayer.getItemInHand(pHand).getItem();
            if (item instanceof BlockItem) {
                //itemInHand can place a block
                BlockState blockState = ((BlockItem) item).getBlock().defaultBlockState();
                //try set direction
                if (blockState.getOptionalValue(BlockStateProperties.FACING).isPresent()) {
                    blockState.setValue(BlockStateProperties.FACING, pState.getValue(direction));
                }
                if (blockState.getShape(pLevel, pPos) == Shapes.block()
                        //block placed by itemInHand is a full block
                        && extinguisherBracketBuiltinEntity.getDisguiseBlockState().getBlock() != ((BlockItem) item).getBlock()) {
                    //block placed by itemInHand is a new block
                    //if (pLevel.isClientSide) {
                        extinguisherBracketBuiltinEntity.setDisguise(blockState);
                    //}
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        } else if (!pLevel.isClientSide) {
            //hit front
            if (pState.getValue(open)) {
                if (pPlayer.isShiftKeyDown()) {
                    //shiftDown right click to close the door
                    pLevel.setBlock(pPos, pState.setValue(open, false), 2);
                } else {
                    //right click to use when opened
                    return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
                }
            } else {
                //right click to open the door when closed
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
        if (state.getValue(open) && player.isCreative()) {
            level.setBlock(pos, state.setValue(open, false), 2);
            return false;
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    /**
     * called when hit in survival to close the door
     */
    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (pState.getValue(open) && !pPlayer.isCreative()) {
            pLevel.setBlock(pPos, pState.setValue(open, false), 2);
            return;
        }
        super.attack(pState, pLevel, pPos, pPlayer);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(open);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide){
            return createTickerHelper(pBlockEntityType, ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY.get(), ExtinguisherBracketBuiltinEntity::serverTick);
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ExtinguisherBracketBuiltinEntity(pPos, pState);
    }


}
