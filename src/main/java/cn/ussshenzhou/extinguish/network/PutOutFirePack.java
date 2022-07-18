package cn.ussshenzhou.extinguish.network;

import cn.ussshenzhou.extinguish.fire.FireHelper;
import cn.ussshenzhou.extinguish.items.ModItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    //------demo only------
    private boolean giveSpark;

    public PutOutFirePack(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.dimension = buf.readResourceLocation();
        //------demo only------
        this.giveSpark = buf.readBoolean();
    }

    public PutOutFirePack(BlockPos blockPos, ResourceLocation dimension) {
        this.blockPos = blockPos;
        this.dimension = dimension;
        //------demo only------
        this.giveSpark = false;
    }

    public PutOutFirePack(BlockPos blockPos, ResourceLocation dimension, boolean giveSpark) {
        this.blockPos = blockPos;
        this.dimension = dimension;
        //------demo only------
        this.giveSpark = giveSpark;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeResourceLocation(dimension);
        //------demo only------
        buf.writeBoolean(giveSpark);
    }


    public void handler(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(
                () -> {
                    if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                        MinecraftServer minecraftServer = (MinecraftServer) LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
                        ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimension);
                        Level level = minecraftServer.getLevel(key);
                        if (level != null) {
                            BlockState blockState = level.getBlockState(blockPos);
                            ServerPlayer player = context.get().getSender();
                            if (FireHelper.putOut(level, blockState, this.blockPos, player)
                                    //------demo only------
                                    && this.giveSpark
                                    && player != null
                                    && Math.random() < 0.333) {
                                player.addItem(new ItemStack(ModItemRegistry.DEMO_ASH.get()));
                            }
                        }
                    } else {
                    }
                }
        );
        context.get().setPacketHandled(true);
    }
}
