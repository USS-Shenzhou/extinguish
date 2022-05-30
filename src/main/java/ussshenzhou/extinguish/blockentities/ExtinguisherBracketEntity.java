package ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Tony Yu
 */
public class ExtinguisherBracketEntity extends BlockEntity {
    private ItemStack itemStack = ItemStack.EMPTY;

    public ExtinguisherBracketEntity(BlockEntityType<ExtinguisherBracketEntity> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ExtinguisherBracketEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("item", itemStack.copy().serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("item", itemStack.copy().serializeNBT());
        return compoundTag;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemStack = ItemStack.of(pTag.getCompound("item"));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.setChanged();
    }
}
