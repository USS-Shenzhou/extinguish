package cn.ussshenzhou.extinguish.network;

import cn.ussshenzhou.extinguish.fire.FireHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

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


    public void handler(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(
                () -> {
                    if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                        MinecraftServer minecraftServer = (MinecraftServer) LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
                        ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimension);
                        Level level = minecraftServer.getLevel(key);
                        if (level != null && level.isLoaded(blockPos)) {
                            BlockState blockState = level.getBlockState(blockPos);
                            ServerPlayer player = context.get().getSender();
                            FireHelper.putOut(level, blockState, this.blockPos, player);
                        }
                    } else {
                    }
                }
        );
        context.get().setPacketHandled(true);
    }
}
