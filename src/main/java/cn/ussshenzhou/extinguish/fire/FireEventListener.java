package cn.ussshenzhou.extinguish.fire;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.WeakHashMap;

/**
 * @author USS_Shenzhou
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FireEventListener {
    /**
     * They said this event is only called on server. But it's not true.
     */
    @SubscribeEvent
    public static void detectFireBlock(BlockEvent.NeighborNotifyEvent event) {
        if (event.getWorld() instanceof ServerLevel level) {
            BlockState blockState = event.getState();
            BlockPos firePos = event.getPos();

            /*if (willPass(firePos)) {
                return;
            }*/

            if (level.dimension() != Level.NETHER && FireHelper.isFire(blockState)) {
                if (!FireManager.fire(level, firePos)) {
                    FireManager.addFireToBuffer(level, firePos);
                }
            }
        }
    }

    //TODO: let player be able to add exception zone
    /*private static boolean willPass(BlockPos blockPos) {
    }*/

    @SubscribeEvent
    public static void onLevelTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.haveTime()) {
            checkFireBufferOnServerTick(event);
        }
    }

    static WeakHashMap<Level, Long> lastWarnTickMap = new WeakHashMap<>();

    private static void checkFireBufferOnServerTick(TickEvent.WorldTickEvent event) {
        Level level = event.world;
        LinkedHashSet<BlockPos> fires = FireManager.getFireBuffer().get(level);
        if (fires != null) {
            long mill = System.currentTimeMillis();
            Iterator<BlockPos> iterator = fires.iterator();
            while (iterator.hasNext()) {
                if (System.currentTimeMillis() > mill + 5) {
                    long tick = level.getGameTime();
                    long lastWarnTick = lastWarnTickMap.getOrDefault(level, 0L);
                    if (Math.abs(tick - lastWarnTick) >= 10) {
                        lastWarnTickMap.put(level, tick);
                        blockPosWarn(fires, level.getGameTime());
                    }
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


    private static void blockPosWarn(LinkedHashSet<BlockPos> fires, long tick) {
        Logger logger = LogManager.getLogger();
        try {
            BlockPos randomPos = fires.toArray(new BlockPos[0])[(int) (fires.size() * Math.random())];
            logger.warn("Checking fire buffer takes to long, costing more than 5 ms, skipping remaining fires...");
            logger.warn("Here is more information about the warn above:");
            logger.warn("Fire buffer's length is " + fires.size() + " .");
            logger.warn("Here is a block randomly chosen from fire buffer.You can check this block at " + randomPos.toShortString() + " in chunk " + (new ChunkPos(randomPos)).toString());

        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @SubscribeEvent
    public static void removeLevel(WorldEvent.Unload event) {
        if (!event.getWorld().isClientSide()) {
            FireManager.removeLevel((Level) event.getWorld());
        }
    }
}
