package cn.ussshenzhou.extinguish.fire;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * @author USS_Shenzhou
 */
public class FireHelper {
    public static boolean isFire(BlockState blockState) {
        return blockState.is(BlockTags.FIRE) || AbstractCandleBlock.isLit(blockState) || CampfireBlock.isLitCampfire(blockState);
    }

    /**
     * @see net.minecraft.world.entity.projectile.ThrownPotion#dowseFire(BlockPos)
     */
    public static void putOut(Level level, BlockState blockState, BlockPos blockPos, @Nullable Entity entity) {
        if (blockState.is(BlockTags.FIRE)) {
            level.removeBlock(blockPos, false);
        } else if (AbstractCandleBlock.isLit(blockState)) {
            AbstractCandleBlock.extinguish((Player) null, blockState, level, blockPos);
        } else if (CampfireBlock.isLitCampfire(blockState)) {
            level.levelEvent((Player) null, 1009, blockPos, 0);
            CampfireBlock.dowse(entity, level, blockPos, blockState);
            level.setBlockAndUpdate(blockPos, blockState.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
        }
    }
    public static boolean canSee(Level level, Vec3 from, Vec3 to) {
        return level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, null)).getType() == HitResult.Type.MISS;
    }
}
