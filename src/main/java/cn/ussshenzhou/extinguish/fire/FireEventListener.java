package cn.ussshenzhou.extinguish.fire;

import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
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
        if (FireHelper.isFire(blockState)) {
            if (!FireManager.fire((ServerLevel) event.getWorld(), event.getPos())) {
                FireManager.addFireToBuffer((ServerLevel) event.getWorld(), event.getPos());
            }
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.haveTime()) {
            addAutoWaterCannonToManager();
            checkFireBufferOnServerTick(event);
        }
    }

    private static void checkFireBufferOnServerTick(TickEvent.WorldTickEvent event) {
        Level level = event.world;
        LinkedHashSet<BlockPos> fires = FireManager.getFireBuffer().get(level);
        if (fires != null) {
            int i = 0;
            Iterator<BlockPos> iterator = fires.iterator();
            while (iterator.hasNext()) {
                BlockPos firePos = iterator.next();
                if (FireHelper.isFire(level.getBlockState(firePos))) {
                    if (FireManager.fire(level, firePos)) {
                        iterator.remove();
                    }
                } else {
                    iterator.remove();
                }
                i++;
                /*if (i >= 10) {
                    return;
                }*/
            }
        }
    }

    private static final LinkedList<AutoWaterCannonEntity> autoWaterCannonEntities = new LinkedList<>();

    public static void addAutoWaterCannonEntity(AutoWaterCannonEntity autoWaterCannonEntity) {
        autoWaterCannonEntities.add(autoWaterCannonEntity);
    }

    private static void addAutoWaterCannonToManager() {
        synchronized (autoWaterCannonEntities){
            if (!autoWaterCannonEntities.isEmpty()) {
                try {
                    for (AutoWaterCannonEntity a : autoWaterCannonEntities) {
                        FireManager.addAutoWaterCannon(a);
                    }
                    autoWaterCannonEntities.clear();
                } catch (NullPointerException ignored){
                    LogManager.getLogger().error("An unexpected Exception happened when trying add Auto Water Cannon from buffer to FireManager list." +
                            " Recommend rebooting the game/server. This rarely happens. If you constantly meet this problem, please contact mod author.");
                }
            }
        }
    }

    @SubscribeEvent
    public static void removeLevel(WorldEvent.Unload event){
        if (!event.getWorld().isClientSide()){
            FireManager.removeLevel((ServerLevel) event.getWorld());
        }
    }
}
