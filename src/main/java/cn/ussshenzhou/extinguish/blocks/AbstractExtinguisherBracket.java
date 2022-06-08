package cn.ussshenzhou.extinguish.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
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
import org.jetbrains.annotations.Nullable;
import cn.ussshenzhou.extinguish.blockentities.AbstractExtinguisherBracketEntity;
import cn.ussshenzhou.extinguish.items.AbstractFireExtinguisher;

/**
 * @author Tony Yu
 */
public abstract class AbstractExtinguisherBracket extends BaseEntityBlock {
    protected static DirectionProperty direction = BlockStateProperties.FACING;

    protected AbstractExtinguisherBracket() {
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
            ItemStack playerStack = pPlayer.getItemInHand(pHand);
            AbstractExtinguisherBracketEntity entity = (AbstractExtinguisherBracketEntity) pLevel.getBlockEntity(pPos);
            if (playerStack.getItem() instanceof AbstractFireExtinguisher) {
                if (entity.isAvailable()) {
                    putIn(entity, pPlayer, pHand, playerStack);
                } else {
                    takeOut(entity, pPlayer);
                }
                return InteractionResult.SUCCESS;
            } else {
                if (!entity.isEmpty()) {
                    takeOut(entity, pPlayer);
                } else {
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void takeOut(AbstractExtinguisherBracketEntity entity, Player pPlayer) {
        ItemStack i = entity.removeItem();
        if (!pPlayer.getInventory().add(i) && !pPlayer.isCreative()) {
            ((ServerPlayer) pPlayer).drop(i, false, false);
        }
    }

    private void putIn(AbstractExtinguisherBracketEntity entity, Player pPlayer, InteractionHand pHand, ItemStack playerStack) {
        entity.addItem(playerStack.copy());
        if (!pPlayer.isCreative()) {
            pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
        }
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
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pLevel.isClientSide && pNewState.getBlock() != pState.getBlock()) {
            AbstractExtinguisherBracketEntity abstractExtinguisherBracketEntity = (AbstractExtinguisherBracketEntity) pLevel.getBlockEntity(pPos);
            abstractExtinguisherBracketEntity.dropContents();
        }
        //super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
