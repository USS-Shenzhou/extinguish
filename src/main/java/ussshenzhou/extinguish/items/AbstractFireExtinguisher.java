package ussshenzhou.extinguish.items;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import ussshenzhou.extinguish.particles.Co2SmokeParticleOption;
import ussshenzhou.extinguish.util.ModItemGroups;

import java.util.Random;

/**
 * @author Tony Yu
 */
public abstract class AbstractFireExtinguisher extends Item {
    private final int maxTime;
    private int duration = 0;

    public AbstractFireExtinguisher(int maxTime) {
        super(new Properties()
                .tab(ModItemGroups.MAIN)
                .stacksTo(1)
                .durability(maxTime)
        );
        this.maxTime = maxTime;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (stack.getDamageValue() < maxTime) {
            duration = maxTime - stack.getDamageValue();
            pPlayer.startUsingItem(pUsedHand);
            stack.getOrCreateTag().putBoolean("using", true);
            return InteractionResultHolder.consume(stack);
        }
        duration = 0;
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        pStack.getOrCreateTag().putBoolean("using", false);
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        pStack.getOrCreateTag().putBoolean("using", false);
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) {
            oldStack.getOrCreateTag().putBoolean("using", false);
        }
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return duration;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxTime;
    }

    //TODO:put out fire
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        stack.setDamageValue(stack.getDamageValue() + 1);
        if (player.level.isClientSide) {
            shootParticle(player);
        }
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    abstract void shootParticle(LivingEntity player);

    void addParticle(ParticleOptions pParticleData, int count, Level level, Vec3 nozzlePos, Vec3 speedVec, float spreadRange, float speed) {
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            level.addParticle(
                    pParticleData,
                    false, nozzlePos.x, nozzlePos.y, nozzlePos.z,
                    speedVec.x * speed + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * spreadRange,
                    speedVec.y * speed + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * spreadRange,
                    speedVec.z * speed + (r.nextBoolean() ? -1 : 1) * r.nextDouble() * spreadRange
            );
        }
    }

    Vec3 getNozzlePosInWorld(LivingEntity player, float tubeLengthIn16) {
        return getNozzlePosInWorld(player, tubeLengthIn16, 0);
    }

    Vec3 getNozzlePosInWorld(LivingEntity player, float tubeLengthIn16, float leftOrRightOffsetIn16) {
        //get shoulder position in world-coordinates.
        LivingEntityRenderer<?, ?> livingEntityRenderer = (LivingEntityRenderer<?, ?>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        HumanoidModel<?> playerModel = ((PlayerRenderer) livingEntityRenderer).getModel();
        Vec3 vec3 = new Vec3(
                Math.cos(Math.PI * player.yBodyRot / 180) * (playerModel.rightArm.x - 1 + leftOrRightOffsetIn16),
                -playerModel.rightArm.y + 22,
                Math.sin(Math.PI * player.yBodyRot / 180) * (playerModel.rightArm.x - 1 + leftOrRightOffsetIn16)
        );
        vec3 = vec3.multiply(1 / 16f, 1 / 16f, 1 / 16f);
        Vec3 playerPos = new Vec3(player.getX(), player.getY(), player.getZ());
        vec3 = playerPos.add(vec3);
        //add arm and item-in-hand to shoulder.
        float armLength = 10 / 16f;
        float xRot = playerModel.rightArm.xRot;
        float yRot = (float) (playerModel.rightArm.yRot + Math.PI * player.yBodyRot / 180);
        //zRot of arm is not necessary to consider.
        tubeLengthIn16 /= 16;
        Vec3 armRot = new Vec3(
                (armLength + tubeLengthIn16) * (Math.sin(xRot) * Math.sin(yRot)),
                (armLength + tubeLengthIn16) * (-Math.cos(xRot)),
                (armLength + tubeLengthIn16) * (-Math.sin(xRot) * Math.cos(yRot))
        );
        //first-person angle optimize
        if (Minecraft.getInstance().player == player && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            return vec3.add(player.getLookAngle().multiply(0.5, 0.5, 0.5));
        }
        return vec3.add(armRot);
    }

    Vec3 getSpeedVec(LivingEntity player) {
        Vec3 speedVec = player.getLookAngle();
        //first-person angle optimize
        /*if (Minecraft.getInstance().player == player && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            return speedVec.multiply(1, 0.8, 1);
        }*/
        return speedVec.multiply(1, 0.8, 1);
    }
}
