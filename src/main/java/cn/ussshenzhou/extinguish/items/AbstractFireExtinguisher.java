package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.blocks.AbstractExtinguisherBracket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import cn.ussshenzhou.extinguish.sounds.MovableSoundInstance;
import cn.ussshenzhou.extinguish.util.ModItemGroups;
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
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Predicate;

/**
 * @author USS_Shenzhou
 */
public abstract class AbstractFireExtinguisher extends Item {
    private final int maxTime;
    private static final Predicate<Entity> ALL_BUT_SPECTATOR = entity -> !entity.isSpectator();
    private static final double INTERACT_DISTANCE = 7;

    MovableSoundInstance soundInstanceBuffer = null;

    public AbstractFireExtinguisher(int maxTime) {
        super(new Properties()
                .tab(ModItemGroups.MAIN)
                .stacksTo(1)
                .durability(maxTime)
        );
        this.maxTime = maxTime;
    }

    /*@Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (!pPlayer.level.isClientSide() && pInteractionTarget instanceof Blaze) {
            this.interactWithBlaze(pStack, pPlayer, (Blaze) pInteractionTarget);
        }
        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }*/

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (stack.getDamageValue() < maxTime - 1) {
            pPlayer.startUsingItem(pUsedHand);
            stack.getOrCreateTag().putBoolean("usingAnime", true);
            if (pLevel.isClientSide) {
                startSound(pLevel, pPlayer);
            }
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof AbstractExtinguisherBracket) {
            return InteractionResult.FAIL;
        }
        return super.useOn(pContext);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        pStack.getOrCreateTag().putBoolean("usingAnime", false);
        if (pLevel.isClientSide) {
            stopSound(pLevel);
        }
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        pStack.getOrCreateTag().putBoolean("usingAnime", false);
        if (pLevel.isClientSide) {
            stopSound(pLevel);
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @OnlyIn(Dist.CLIENT)
    abstract protected void startSound(Level pLevel, Player pPlayer);

    @OnlyIn(Dist.CLIENT)
    private void stopSound(Level level) {
        if (level.isClientSide && soundInstanceBuffer != null) {
            Minecraft.getInstance().getSoundManager().stop(soundInstanceBuffer);
            soundInstanceBuffer = null;
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) {
            oldStack.getOrCreateTag().putBoolean("usingAnime", false);
        }
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return maxTime - pStack.getDamageValue();
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxTime;
    }

    private Entity interactBuffer = null;
    protected int interactCounter = 0;

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        stack.setDamageValue(stack.getDamageValue() + 1);
        if (player.level.isClientSide) {
            shootParticle(player);
        } else {
            if (player instanceof ServerPlayer serverPlayer) {
                Vec3 from = serverPlayer.getEyePosition();
                Vec3 extend = player.getViewVector(1).scale(6.5);
                Vec3 to = from.add(extend);
                EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(player, from, to,
                        player.getBoundingBox().expandTowards(extend).inflate(1.0D),
                        ALL_BUT_SPECTATOR,
                        INTERACT_DISTANCE * INTERACT_DISTANCE
                );
                if (entityHitResult == null) {
                    interactBuffer = null;
                    interactCounter = 0;
                } else {
                    Entity entity = entityHitResult.getEntity();
                    if (interactBuffer == null || interactBuffer.getId() != entity.getId()) {
                        interactBuffer = entity;
                        interactCounter = 1;
                    } else {
                        interactCounter++;
                    }
                    doInteract();
                }
            }
        }
    }

    protected void doInteract() {
        if (interactCounter >= 5) {
            if (interactBuffer instanceof Blaze) {
                interactWithBlaze((Blaze) interactBuffer);
            } else if (interactBuffer instanceof Player) {
                interactWithPlayer((Player) interactBuffer);
            } else {
                interactWithOtherEntity(interactBuffer);
            }
        }
    }

    protected abstract void interactWithOtherEntity(Entity entity);

    protected void interactWithPlayer(Player player) {
        interactWithOtherEntity(player);
    }

    protected void interactWithBlaze(Blaze blaze) {
        blaze.setTarget(null);
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
            return vec3.add(player.getLookAngle().multiply(1, 1, 1));
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
