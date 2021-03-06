package cn.ussshenzhou.extinguish.fire;

import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
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

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
                LogManager.getLogger().warn("Fire buffer is too big with a amount of "
                        + fires.size()
                        + " in world "
                        + level.toString()
                        + " | "
                        + level.dimension());
            }
            long mill = System.currentTimeMillis();
            Iterator<BlockPos> iterator = fires.iterator();
            while (iterator.hasNext()) {
                if (System.currentTimeMillis() > mill + 5) {
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

    @SubscribeEvent
    public static void removeLevel(WorldEvent.Unload event) {
        if (!event.getWorld().isClientSide()) {
            FireManager.removeLevel((ServerLevel) event.getWorld());
        }
    }
}
