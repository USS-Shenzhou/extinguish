package cn.ussshenzhou.extinguish.blocks;

import cn.ussshenzhou.extinguish.blockentities.ExtinguisherBracketBuiltinEntity;
import cn.ussshenzhou.extinguish.render.RawQuad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            ExtinguisherBracketBuiltinEntity extinguisherBracketBuiltinEntity = (ExtinguisherBracketBuiltinEntity) pLevel.getBlockEntity(pPos);
            if (pPlayer.getItemInHand(pHand).getItem() instanceof BlockItem) {
                if (pLevel.isClientSide ) {
                    extinguisherBracketBuiltinEntity.setDisguise((BlockItem) pPlayer.getItemInHand(pHand).getItem(), pLevel, pPos);
                }
                return InteractionResult.SUCCESS;
            }
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
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ExtinguisherBracketBuiltinEntity(pPos, pState);
    }
}
