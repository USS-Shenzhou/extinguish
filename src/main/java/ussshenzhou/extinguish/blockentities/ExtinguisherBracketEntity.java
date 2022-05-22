package ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Tony Yu
 */
public class ExtinguisherBracketEntity extends BlockEntity {
    public ExtinguisherBracketEntity(BlockEntityType<ExtinguisherBracketEntity> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ExtinguisherBracketEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }
}
