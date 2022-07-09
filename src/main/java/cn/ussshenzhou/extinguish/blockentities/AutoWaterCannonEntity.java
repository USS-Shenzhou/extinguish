package cn.ussshenzhou.extinguish.blockentities;

import cn.ussshenzhou.extinguish.fire.FireEventListener;
import cn.ussshenzhou.extinguish.fire.FireHelper;
import cn.ussshenzhou.extinguish.fire.FireManager;
import cn.ussshenzhou.extinguish.network.PreciseParticlePack;
import cn.ussshenzhou.extinguish.network.PreciseParticlePackSend;
import cn.ussshenzhou.extinguish.particles.WaterSpoutParticleOption;
import cn.ussshenzhou.extinguish.sounds.ModSoundsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;

import java.util.Random;
import java.util.function.Predicate;

/**
 * @author USS_Shenzhou
 */
public class AutoWaterCannonEntity extends BlockEntity implements ISyncFromServer {
    private static final float PI = (float) Math.PI;
    //all angles in radian

    private float pitch = 0;
    private float yaw = 0;
    private float prevPitch = 0;
    private float prevYaw = 0;
    private float pitchGoal = 0;
    private float yawGoal = 0;
    private boolean pitchReady = false;
    private boolean yawReady = false;
    private BlockPos target;
    private int shootTicker = -1;
    private boolean unattended = false;
    //private static final Predicate<ServerPlayer> ALL_SERVER_PLAYER = serverPlayer -> serverPlayer != null;
    private static final float NOZZLE_LENGTH = 8 / 16f;
    private static final float MAX_SPEED = 1.4f;
    private static final float MIN_SPEED = 0.3f;
    private static final float MAX_DIFFUSE = 0.13f;
    private static final float MIN_DIFFUSE = 0.03f;
    private static final float MAX_PITCH_RATE = (float) Math.toRadians(2);
    private static final float MAX_YAW_RATE = (float) Math.toRadians(3);
    private static final int MAX_RANGE = 16;
    private static final int SHOOT_TIME = 15;
    private final Random random = new Random();


    public AutoWaterCannonEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        FireEventListener.addAutoWaterCannonEntity(this);
    }

    public AutoWaterCannonEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AutoWaterCannonEntity thisEntity) {
        boolean inNether = level.dimension() == Level.NETHER;
        if (thisEntity.pitchReady && thisEntity.yawReady && thisEntity.busy() && !inNether) {
            if (thisEntity.shootTicker == -1) {
                //start
                thisEntity.shootTicker = 0;
                level.playSound(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, ModSoundsRegistry.CANNON_SHOOT.get(), SoundSource.BLOCKS, 1, 1);
            } else if (thisEntity.shootTicker > SHOOT_TIME) {
                //end
                thisEntity.shootTicker = -1;
                if (thisEntity.unattended) {
                    FireHelper.putOut(level, level.getBlockState(thisEntity.target), thisEntity.target, null);
                }
                //thisEntity.pitchGoal = 0;
                //thisEntity.yawGoal = 0;
                thisEntity.target = null;
            } else {
                //shooting
                thisEntity.shootWater();
                thisEntity.shootTicker++;
            }
        } else if (inNether && thisEntity.random.nextFloat() < 0.05) {
            //go mad in nether
            thisEntity.setPitchGoal(thisEntity.random.nextFloat(-PI / 2, (float) Math.toRadians(30)));
            thisEntity.setYawGoal(thisEntity.random.nextFloat(-PI, PI));
        }
        thisEntity.checkPitch();
        thisEntity.checkYaw();
        thisEntity.syncFromServer(level, thisEntity);
    }

    private void shootWater() {
        ServerLevel serverLevel = (ServerLevel) this.level;
        Player player = serverLevel.getNearestPlayer(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), 100, false);
        unattended = (player == null);
        Vec3 pos = getNozzlePos();
        double distance = Math.sqrt(getBlockPos().distToLowCornerSqr(target.getX(), target.getY(), target.getZ()));
        double adjustedSpeed = getAdjustedSpeed(distance);
        Vec3 speed = getDirectionNormalVector().multiply(adjustedSpeed, adjustedSpeed, adjustedSpeed);
        for (ServerPlayer p : serverLevel.getPlayers(ServerPlayer::isAlive)) {
            PreciseParticlePackSend.channel.send(
                    PacketDistributor.PLAYER.with(() -> p),
                    new PreciseParticlePack(new WaterSpoutParticleOption(
                            (player != null && player.getUUID() == p.getUUID()) ? player.getUUID() : null),
                            pos.x, pos.y, pos.z, 0.01, speed.x, speed.y, speed.z, getAdjustedDiffuse(distance), 3)
            );
        }
    }

    private Vec3 getNozzlePos() {
        Vec3 v = new Vec3(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ());

        v = v.add(0.5, isDown() ? 9 / 16d : 7 / 16d, 0.5);
        v = v.add(getDirectionNormalVector().multiply(NOZZLE_LENGTH, NOZZLE_LENGTH, NOZZLE_LENGTH));
        return v;
    }

    private Vec3 getDirectionNormalVector() {
        return new Vec3(Math.sin(yaw) * Math.cos(pitch), Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch));
    }

    private double getAdjustedSpeed(double distance) {
        return MIN_SPEED + (distance / MAX_RANGE) * (MAX_SPEED - MIN_SPEED);
    }

    private double getAdjustedDiffuse(double distance) {
        return MIN_DIFFUSE + (1 - distance / MAX_RANGE) * (MAX_DIFFUSE - MIN_DIFFUSE);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AutoWaterCannonEntity thisEntity) {
        thisEntity.prevPitch = thisEntity.pitch;
        thisEntity.prevYaw = thisEntity.yaw;
    }

    private boolean isDown() {
        return this.getBlockState().getValue(BlockStateProperties.FACING) == Direction.DOWN;
    }


    public boolean setTarget(BlockPos targetPos) {
        double distance = Math.sqrt(getBlockPos().distToLowCornerSqr(targetPos.getX(), targetPos.getY(), targetPos.getZ()));
        Vec3 vt = new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ());
        BlockState targetBlock = this.level.getBlockState(targetPos);
        if (targetBlock.getBlock() == Blocks.FIRE) {
            vt = vt.add(FireHelper.getFireCenter(targetBlock));
        } else {
            vt = vt.add(0.5,0.2,0.5);
        }
        Vec3 v = new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5);
        if (distance < MAX_RANGE && !busy() && canSee(vt, v)) {
            Vec3 dv = vt.add(-v.x, -v.y, -v.z);
            float pGoal = (float) Math.atan(dv.y / Math.sqrt(dv.x * dv.x + dv.z * dv.z));
            //gravity compensate
            //pGoal += getGravityCompensate(distance, dv.y);
            if (!setPitchGoal(pGoal)) {
                return false;
            }
            float f = (float) Math.atan(dv.x / dv.z);
            if (dv.z >= 0) {
                setYawGoal(f);
            } else if (f < 0) {
                setYawGoal(PI + f);
            } else {
                setYawGoal(f - PI);
            }
            this.target = targetPos;
            return true;
        }
        return false;
    }

    private float getGravityCompensate(double distance, double dy) {
        //(float) ((distance / MAX_RANGE) * Math.toRadians(isDown() ? 4 : 10))
        double distanceCompensate = 0;
        //Mth.lerp(distance/MAX_RANGE,);
        double yCompensate;
        if (dy <= 0) {
            LogManager.getLogger().debug(dy / -2);
            yCompensate = Mth.lerp(Math.min(dy / -2, 1), -10, 0);
        } else {
            yCompensate = Mth.lerp(dy / 8, -10, 0);
        }
        return (float) Math.toRadians(distanceCompensate + yCompensate);
    }

    private boolean canSee(Vec3 vt, Vec3 v) {
        return FireHelper.canSee(level, vt, v.add(-0.3, 0, -0.3)) || FireHelper.canSee(level, vt, v.add(0.3, 0, 0.3));
    }

    public boolean busy() {
        return this.target != null;
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

    private boolean setPitchGoal(float pitchGoal) {
        float minPitch = isDown() ? -PI / 2 - 0.0001f : (float) Math.toRadians(-30);
        float maxPitch = isDown() ? (float) Math.toRadians(30) : PI / 2;
        if (pitchGoal < maxPitch && pitchGoal > minPitch) {
            this.pitchGoal = pitchGoal;
            pitchReady = false;
            return true;
        } else {
            return false;
        }
    }

    private boolean setYawGoal(float yawGoal) {
        if (yawGoal <= PI && yawGoal >= -PI) {
            this.yawGoal = yawGoal;
            yawReady = false;
            return true;
        } else {
            return false;
        }
    }

    private void checkPitch() {
        float f = Math.abs(pitch - pitchGoal);
        if (f > 0.00001) {
            if (f < MAX_PITCH_RATE) {
                pitch = pitchGoal;
            } else {
                if (pitch < pitchGoal) {
                    pitch += MAX_PITCH_RATE;
                } else {
                    pitch -= MAX_PITCH_RATE;
                }
            }
        } else {
            pitchReady = true;
        }
    }

    private void checkYaw() {
        float f = Math.abs(yaw - yawGoal);
        if (f > 0.00001) {
            if (f < MAX_YAW_RATE) {
                yaw = yawGoal;
            } else {
                if (yaw < yawGoal) {
                    yaw += MAX_YAW_RATE;
                } else {
                    yaw -= MAX_YAW_RATE;
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

    @Override
    public void setRemoved() {
        FireManager.removeAutoWaterCannon(this);
        super.setRemoved();
    }

    @Override
    public int hashCode() {
        return this.level.dimension().location().hashCode() + this.getBlockPos().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AutoWaterCannonEntity)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ((AutoWaterCannonEntity) obj).level.dimension().location().equals(this.level.dimension().location()) && ((AutoWaterCannonEntity) obj).getBlockPos().equals(this.getBlockPos());
    }
}