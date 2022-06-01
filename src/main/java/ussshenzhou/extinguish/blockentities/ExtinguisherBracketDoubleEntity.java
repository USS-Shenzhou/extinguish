package ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Tony Yu
 */
public class ExtinguisherBracketDoubleEntity extends AbstractExtinguisherBracketEntity {
    private ItemStack[] itemStacks = {ItemStack.EMPTY, ItemStack.EMPTY};

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
        CompoundTag compoundTag = new CompoundTag();
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
        sync();
    }

    @Override
    public ItemStack removeItem() {
        ItemStack i;
        if (!itemStacks[0].isEmpty()) {
            i = itemStacks[0];
            itemStacks[0] = ItemStack.EMPTY;
        } else {
            i = itemStacks[1];
            itemStacks[1] = ItemStack.EMPTY;
        }
        sync();
        return i;
    }

}
