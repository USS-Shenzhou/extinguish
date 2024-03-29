package cn.ussshenzhou.extinguish.fire;

import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author USS_Shenzhou
 * <br/>
 * Only on logic server.
 * <br/>
 * idea: use WeakReference, by @ustc-zzzz
 */
public class FireManager {
    private static final ConcurrentHashMap<Level, ConcurrentHashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>>> autoWaterCannons = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Level, LinkedHashSet<BlockPos>> fireBuffer = new ConcurrentHashMap<>();

    public static void addAutoWaterCannon(AutoWaterCannonEntity autoWaterCannonEntity) {
        Level level = autoWaterCannonEntity.getLevel();
        checkLevel(level);
        ChunkPos c = new ChunkPos(autoWaterCannonEntity.getBlockPos());
        ConcurrentHashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>> h = autoWaterCannons.get(level);
        if (h.containsKey(c)) {
            h.get(c).add(autoWaterCannonEntity);
        } else {
            h.put(c, new LinkedHashSet<>(List.of(autoWaterCannonEntity)));
        }
    }

    public static void removeAutoWaterCannon(AutoWaterCannonEntity autoWaterCannonEntity) {
        ConcurrentHashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>> h = autoWaterCannons.get(autoWaterCannonEntity.getLevel());
        if (h != null) {
            ChunkPos c = new ChunkPos(autoWaterCannonEntity.getBlockPos());
            if (h.get(c) != null) {
                h.get(c).remove(autoWaterCannonEntity);
            } else {
                LogManager.getLogger().error("Failed trying remove Auto Water Cannon at "
                        + autoWaterCannonEntity.getBlockPos().toShortString()
                        + "from FireManager list. The chunk map is null. If you want, I recommend rebooting the game/server. "
                        + "This rarely happens. If you constantly meet this problem, please contact mod author.");
            }
        }
    }

    @Deprecated
    public static void removeAutoWaterCannon(Level level, BlockPos blockPos) {
        AutoWaterCannonEntity a = (AutoWaterCannonEntity) level.getBlockEntity(blockPos);
        if (a != null) {
            removeAutoWaterCannon(a);
        }
    }

    public static boolean fire(Level level, BlockPos firePos) {
        checkLevel(level);
        ConcurrentHashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>> h = autoWaterCannons.get(level);
        ChunkPos fireChunk = new ChunkPos(firePos);
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                ChunkPos chunkPos = new ChunkPos(fireChunk.x + x, fireChunk.z + z);
                if (h.containsKey(chunkPos)) {
                    LinkedHashSet<AutoWaterCannonEntity> autoWaterCannonEntities = h.get(chunkPos);
                    for (AutoWaterCannonEntity a : autoWaterCannonEntities) {
                        if (a.setTarget(firePos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void addFireToBuffer(Level level, BlockPos blockPos) {
        checkLevel(level);
        LinkedHashSet<BlockPos> fireList = fireBuffer.get(level);
        fireList.add(blockPos);
    }

    private static void checkLevel(Level level) {
        if (!autoWaterCannons.containsKey(level)) {
            autoWaterCannons.put(level, new ConcurrentHashMap<>());
        }
        if (!fireBuffer.containsKey(level)) {
            fireBuffer.put(level, new LinkedHashSet<>());
        }
    }

    public static ConcurrentHashMap<Level, LinkedHashSet<BlockPos>> getFireBuffer() {
        return fireBuffer;
    }

    public static ConcurrentHashMap<Level, ConcurrentHashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>>> getAutoWaterCannons() {
        return autoWaterCannons;
    }

    public static void removeLevel(Level level) {
        autoWaterCannons.remove(level);
        fireBuffer.remove(level);
    }
}
