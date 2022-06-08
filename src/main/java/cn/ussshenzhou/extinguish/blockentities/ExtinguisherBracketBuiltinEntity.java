package cn.ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherBracketBuiltinEntity extends ExtinguisherBracketDoubleEntity{

    public ExtinguisherBracketBuiltinEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ExtinguisherBracketBuiltinEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY.get(),pWorldPosition, pBlockState);
    }
}
