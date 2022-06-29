package cn.ussshenzhou.extinguish.blockentities;

import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author USS_Shenzhou
 */
public interface ISyncFromServer {
    default void syncFromServer(Level level, BlockEntity blockEntity) {
        if (!level.isClientSide) {
            ClientboundBlockEntityDataPacket p = ClientboundBlockEntityDataPacket.create(blockEntity);
            ((ServerLevel) level).getChunkSource().chunkMap.getPlayers(new ChunkPos(blockEntity.getBlockPos()), false).forEach(
                    k -> k.connection.send(p)
            );
            blockEntity. setChanged();
        }
    }
}
