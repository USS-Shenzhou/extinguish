package cn.ussshenzhou.extinguish.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;

import java.util.Random;

/**
 * @author USS_Shenzhou
 */
public class AutoWaterCannonEntity extends BlockEntity implements ISyncFromServer {
    private static final float PI = (float) Math.PI;
    private static final float MAX_DELTA = (float) Math.toRadians(2);
    //all angles in radian

    private float pitch = 0;
    private float yaw = 0;
    private float prevPitch = 0;
    private float prevYaw = 0;
    private float pitchGoal = 0;
    private float yawGoal = 0;
    private boolean pitchReady = false;
    private boolean yawReady = false;

    public AutoWaterCannonEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public AutoWaterCannonEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }


    public static void serverTick(Level level, BlockPos pos, BlockState state, AutoWaterCannonEntity thisEntity) {





        thisEntity.checkPitch();
        thisEntity.checkYaw();
        thisEntity.syncFromServer(level, thisEntity);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AutoWaterCannonEntity thisEntity) {
        thisEntity.prevPitch = thisEntity.pitch;
        thisEntity.prevYaw = thisEntity.yaw;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("pitch", pitch);
        pTag.putFloat("yaw", yaw);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.putFloat("pitch", pitch);
        compoundTag.putFloat("yaw", yaw);
        return compoundTag;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.pitch = pTag.getFloat("pitch");
        this.yaw = pTag.getFloat("yaw");
    }

    private void setPitchGoal(float pitchGoal) {
        this.pitchGoal = Mth.clamp(pitchGoal, -PI / 2, (float) Math.toRadians(40));
        pitchReady = false;
    }

    private void setYawGoal(float yawGoal) {
        this.yawGoal = Mth.clamp(yawGoal, -PI, PI);
        yawReady = false;
    }

    private void checkPitch() {
        float f = Math.abs(pitch - pitchGoal);
        if (f > 0.00001) {
            if (f < MAX_DELTA) {
                pitch = pitchGoal;
            } else {
                if (pitch < pitchGoal) {
                    pitch += MAX_DELTA;
                } else {
                    pitch -= MAX_DELTA;
                }
            }
        } else {
            pitchReady = true;
        }
    }

    private void checkYaw() {
        float f = Math.abs(yaw - yawGoal);
        if (f > 0.00001) {
            if (f < MAX_DELTA) {
                yaw = yawGoal;
            } else {
                /*//choose closer direction to reach the goal.
                if (f > PI) {
                    if (yaw < yawGoal) {
                        yaw -= MAX_DELTA;
                    } else {
                        yaw += MAX_DELTA;
                    }
                } else {
                    if (yaw < yawGoal) {
                        yaw += MAX_DELTA;
                    } else {
                        yaw -= MAX_DELTA;
                    }
                }
                //check if yaw too big
                if (yaw > PI) {
                    yaw -= PI;
                } else if (yaw < -PI) {
                    yaw += PI;
                }*/
                if (yaw < yawGoal) {
                    yaw += MAX_DELTA;
                } else {
                    yaw -= MAX_DELTA;
                }
            }
        } else {
            yawReady = true;
        }
    }


    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPrevPitch() {
        return prevPitch;
    }

    public float getPrevYaw() {
        return prevYaw;
    }
}
