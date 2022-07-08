package cn.ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author USS_Shenzhou
 */
public class AutoWaterCannonEntityWhite extends AutoWaterCannonEntity {

    public AutoWaterCannonEntityWhite(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public AutoWaterCannonEntityWhite(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY_WHITE.get(), pWorldPosition, pBlockState);
    }
}
