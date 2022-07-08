package cn.ussshenzhou.extinguish.fire;

import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.*;


/**
 * @author USS_Shenzhou
 * <br/>
 * Only on logic server.
 */
public class FireManager {
    private static HashMap<Level, HashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>>> autoWaterCannons = new HashMap<>();

    private static HashMap<Level, LinkedHashSet<BlockPos>> fireBuffer = new HashMap<>();

    public static void addAutoWaterCannon(AutoWaterCannonEntity autoWaterCannonEntity) {
        Level level = autoWaterCannonEntity.getLevel();
        checkLevel(level);
        ChunkPos c = new ChunkPos(autoWaterCannonEntity.getBlockPos());
        HashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>> h = autoWaterCannons.get(level);
        if (h.containsKey(c)) {
            h.get(c).add(autoWaterCannonEntity);
        } else {
            h.put(c, new LinkedHashSet<>(List.of(autoWaterCannonEntity)));
        }
    }

    public static void removeAutoWaterCannon(AutoWaterCannonEntity autoWaterCannonEntity) {
        HashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>> h = autoWaterCannons.get(autoWaterCannonEntity.getLevel());
        if (h != null) {
            ChunkPos c = new ChunkPos(autoWaterCannonEntity.getBlockPos());
            h.get(c).remove(autoWaterCannonEntity);
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
        HashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>> h = autoWaterCannons.get(level);
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

    public static void removeFireFromBuffer(Level level, BlockPos blockPos) {
        checkLevel(level);
        LinkedHashSet<BlockPos> fireList = fireBuffer.get(level);
        fireList.remove(blockPos);
    }

    private static void checkLevel(Level level) {
        if (!autoWaterCannons.containsKey(level)) {
            autoWaterCannons.put(level, new HashMap<>());
        }
        if (!fireBuffer.containsKey(level)) {
            fireBuffer.put(level, new LinkedHashSet<>());
        }
    }

    public static HashMap<Level, LinkedHashSet<BlockPos>> getFireBuffer() {
        return fireBuffer;
    }

    public static HashMap<Level, HashMap<ChunkPos, LinkedHashSet<AutoWaterCannonEntity>>> getAutoWaterCannons() {
        return autoWaterCannons;
    }
}
