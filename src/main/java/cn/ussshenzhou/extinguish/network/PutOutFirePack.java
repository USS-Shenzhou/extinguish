package cn.ussshenzhou.extinguish.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

/**
 * @author USS_Shenzhou
 */
public class PutOutFirePack {
    private BlockPos blockPos;
    private ResourceLocation dimension;

    public PutOutFirePack(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.dimension = buf.readResourceLocation();
    }

    public PutOutFirePack(BlockPos blockPos, ResourceLocation dimension) {
        this.blockPos = blockPos;
        this.dimension = dimension;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeResourceLocation(dimension);
    }

    /**
     * @see net.minecraft.world.entity.projectile.ThrownPotion#dowseFire(BlockPos)
     */
    public void handler(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(
                () -> {
                    if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                        MinecraftServer minecraftServer = (MinecraftServer) LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
                        ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY,dimension);
                        Level level = minecraftServer.getLevel(key);
                        if (level != null) {
                            BlockState blockState = level.getBlockState(blockPos);
                            if (blockState.is(BlockTags.FIRE)) {
                                level.removeBlock(blockPos, false);
                            } else if (AbstractCandleBlock.isLit(blockState)) {
                                AbstractCandleBlock.extinguish((Player)null, blockState, level, blockPos);
                            } else if (CampfireBlock.isLitCampfire(blockState)) {
                                level.levelEvent((Player)null, 1009, blockPos, 0);
                                CampfireBlock.dowse(context.get().getSender(), level, blockPos, blockState);
                                level.setBlockAndUpdate(blockPos, blockState.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
                            }
                        }
                    } else {
                    }
                }
        );
        context.get().setPacketHandled(true);
    }
}
