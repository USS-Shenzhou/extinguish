package cn.ussshenzhou.extinguish.fire;

import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author USS_Shenzhou
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FireEventListener {
    @SubscribeEvent
    public static void detectFireBlock(BlockEvent.NeighborNotifyEvent event) {
        //NeighborNotifyEvent only called on server
        BlockState blockState = event.getState();
        if (((Level) event.getWorld()).dimension() != Level.NETHER && FireHelper.isFire(blockState)) {
            if (!FireManager.fire((ServerLevel) event.getWorld(), event.getPos())) {
                FireManager.addFireToBuffer((ServerLevel) event.getWorld(), event.getPos());
            }
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.haveTime()) {
            checkFireBufferOnServerTick(event);
        }
    }

    private static void checkFireBufferOnServerTick(TickEvent.WorldTickEvent event) {
        Level level = event.world;
        LinkedHashSet<BlockPos> fires = FireManager.getFireBuffer().get(level);
        if (fires != null) {
            if (fires.size() > 200) {
                LogManager.getLogger().warn("Fire buffer is too big in world "
                        + level.toString()
                        + " | "
                        + level.dimension());
                blockPosWarn(fires);
            }
            long mill = System.currentTimeMillis();
            Iterator<BlockPos> iterator = fires.iterator();
            while (iterator.hasNext()) {
                if (System.currentTimeMillis() > mill + 5) {
                    LogManager.getLogger().warn("Checking fire buffer is too slow, costing more than 5 ms, skipping remaining fires...");
                    blockPosWarn(fires);
                    break;
                }
                BlockPos firePos = iterator.next();
                if (!level.isLoaded(firePos)) {
                    continue;
                }
                if (FireHelper.isFire(level.getBlockState(firePos))) {
                    if (FireManager.fire(level, firePos)) {
                        iterator.remove();
                    }
                } else {
                    iterator.remove();
                }
            }
        }
    }

    private static void blockPosWarn(LinkedHashSet<BlockPos> fires) {
        Logger logger = LogManager.getLogger();
        try{
            BlockPos randomPos = fires.toArray(new BlockPos[0])[(int) (fires.size() * Math.random())];
            logger.warn("Here is more information about the warn above:");
            logger.warn("Fire buffer's length is "+fires.size()+" .");
            logger.warn("Here is a block randomly chosen from fire buffer.You can check this block at "+randomPos.toShortString()+" in chunk "+(new ChunkPos(randomPos)).toString());
        } catch (IndexOutOfBoundsException ignored){}
    }

    @SubscribeEvent
    public static void removeLevel(WorldEvent.Unload event) {
        if (!event.getWorld().isClientSide()) {
            FireManager.removeLevel((ServerLevel) event.getWorld());
        }
    }
}
