package cn.ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Tony Yu
 */
public class ExtinguisherBracketSingleEntity extends AbstractExtinguisherBracketEntity {
    private ItemStack itemStack = ItemStack.EMPTY;

    public ExtinguisherBracketSingleEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ExtinguisherBracketSingleEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_SINGLE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("item", itemStack.copy().serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.put("item", itemStack.copy().serializeNBT());
        return compoundTag;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemStack = ItemStack.of(pTag.getCompound("item"));
    }

    @Override
    public boolean isAvailable() {
        return itemStack.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return itemStack.isEmpty();
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void addItem(ItemStack itemStackCopy) {
        this.itemStack = itemStackCopy;
        syncFromServer(level,this);
    }

    @Override
    public ItemStack removeItem() {
        ItemStack i = this.itemStack;
        this.itemStack = ItemStack.EMPTY;
        syncFromServer(level,this);
        return i;
    }

    @Override
    public void dropContents() {
        Containers.dropItemStack(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), itemStack);
    }
}
