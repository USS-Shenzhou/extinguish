package cn.ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherBracketDoubleEntity extends AbstractExtinguisherBracketEntity{
    protected ItemStack[] itemStacks = {ItemStack.EMPTY, ItemStack.EMPTY};

    public ExtinguisherBracketDoubleEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ExtinguisherBracketDoubleEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntityTypeRegistry.EXTINGUISHER_BRACKET_DOUBLE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("item0", itemStacks[0].copy().serializeNBT());
        pTag.put("item1", itemStacks[1].copy().serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.put("item0", itemStacks[0].copy().serializeNBT());
        compoundTag.put("item1", itemStacks[1].copy().serializeNBT());
        return compoundTag;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemStacks[0] = ItemStack.of(pTag.getCompound("item0"));
        this.itemStacks[1] = ItemStack.of(pTag.getCompound("item1"));
    }

    @Override
    public boolean isAvailable() {
        return itemStacks[0].isEmpty() || itemStacks[1].isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return itemStacks[0].isEmpty() && itemStacks[1].isEmpty();
    }

    public ItemStack[] getItemStacks() {
        return itemStacks;
    }

    @Override
    public void addItem(ItemStack itemStackCopy) {
        if (itemStacks[0].isEmpty()) {
            itemStacks[0] = itemStackCopy;
        } else {
            itemStacks[1] = itemStackCopy;
        }
        syncFromServer(level,this);
    }

    @Override
    public ItemStack removeItem() {
        ItemStack i;
        if (!itemStacks[0].isEmpty()) {
            i = itemStacks[0].copy();
            itemStacks[0] = ItemStack.EMPTY;
        } else {
            i = itemStacks[1].copy();
            itemStacks[1] = ItemStack.EMPTY;
        }
        syncFromServer(level,this);
        return i;
    }

    @Override
    public void dropContents() {
        Containers.dropItemStack(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(),itemStacks[0]);
        Containers.dropItemStack(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(),itemStacks[1]);
    }

}
