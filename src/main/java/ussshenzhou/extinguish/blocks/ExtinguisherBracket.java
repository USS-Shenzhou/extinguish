package ussshenzhou.extinguish.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import ussshenzhou.extinguish.blockentities.ExtinguisherBracketEntity;
import ussshenzhou.extinguish.items.AbstractFireExtinguisher;

/**
 * @author Tony Yu
 */
public class ExtinguisherBracket extends BaseEntityBlock {
    public static DirectionProperty direction = BlockStateProperties.FACING;
    private static final VoxelShape NORTH = Block.box(6, 0, 11.5, 10, 10, 16);
    private static final VoxelShape SOUTH = Block.box(6, 0, 0, 10, 10, 4.5);
    private static final VoxelShape WEST = Block.box(11.5, 0, 6, 16, 10, 10);
    private static final VoxelShape EAST = Block.box(0, 0, 6, 4.5, 10, 10);

    public ExtinguisherBracket() {
        super(
                Properties.of(Material.METAL)
                        .noOcclusion()
                        .strength(1, 5)
        );
        this.registerDefaultState(this.getStateDefinition().any().setValue(direction, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            ItemStack itemStack = pPlayer.getItemInHand(pHand);
            ExtinguisherBracketEntity entity = (ExtinguisherBracketEntity) pLevel.getBlockEntity(pPos);
            if (entity.getItemStack().isEmpty()) {
                if (itemStack.getItem() instanceof AbstractFireExtinguisher){
                    entity.setItemStack(itemStack.copy());
                    pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            } else {
                pPlayer.getInventory().add(entity.getItemStack());
                entity.setItemStack(ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
        //return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(direction, pContext.getHorizontalDirection().getOpposite());
    }

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

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ExtinguisherBracketEntity(pPos, pState);
    }

}
