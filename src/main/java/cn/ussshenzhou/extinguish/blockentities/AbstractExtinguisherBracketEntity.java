package cn.ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Tony Yu
 */
public abstract class AbstractExtinguisherBracketEntity extends BlockEntity {
    public AbstractExtinguisherBracketEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    abstract public boolean isAvailable();

    abstract public boolean isEmpty();

    abstract public void addItem(ItemStack itemStackCopy);

    abstract public ItemStack removeItem();

    abstract public void dropContents();

    protected void sync() {
        if (!level.isClientSide) {
            ClientboundBlockEntityDataPacket p = ClientboundBlockEntityDataPacket.create(this);
            ((ServerLevel) level).getChunkSource().chunkMap.getPlayers(new ChunkPos(getBlockPos()), false).forEach(
                    k -> k.connection.send(p)
            );
            setChanged();
        }
    }
}
