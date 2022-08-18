package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.particles.WaterSpoutParticleOption;
import cn.ussshenzhou.extinguish.sounds.ModSoundsRegistry;
import cn.ussshenzhou.extinguish.sounds.FollowingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author USS_Shenzhou
 */
public class FireExtinguisherWater extends AbstractFireExtinguisher {
    public FireExtinguisherWater() {
        super(12 * 20);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void playClientSound(Level pLevel, Player pPlayer) {
        if (pLevel.isClientSide) {
            pLevel.playLocalSound(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSoundsRegistry.DRY_START.get(), SoundSource.PLAYERS, 0.8f, 0.9f, false);
            FollowingSoundInstance f;
            if (pPlayer.level.dimension() != Level.NETHER) {
                f = new FollowingSoundInstance(pPlayer, ModSoundsRegistry.DRY_SHOOT.get(), SoundSource.PLAYERS, 0.8f, 0.9f, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
            } else {
                f = new FollowingSoundInstance(pPlayer, ModSoundsRegistry.DRY_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 0.9f, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
            }
            Minecraft.getInstance().getSoundManager().play(f);
        }
    }

    @Override
    public SoundEvent getShootSound() {
        return ModSoundsRegistry.DRY_SHOOT.get();
    }

    @Override
    protected void interactWithOtherEntity(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, interactCounter * 10, 0));
        if (interactCounter % 20 == 0) {
            entity.hurt(DamageSource.MAGIC, 1f);
        }
    }

    @Override
    protected void interactWithBlaze(Blaze blaze) {
        if (blaze.level.dimension() != Level.NETHER) {
            blaze.setTarget(null);
            if (interactCounter % 20 == 0) {
                blaze.hurt(DamageSource.DROWN, 6f);
            }
        }
    }

    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 5);
        Vec3 speedVec = getSpeedVec(player);
        if (player.level.dimension() != Level.NETHER) {
            addParticle(new WaterSpoutParticleOption(player.getUUID()), 3, player.level, nozzlePos, speedVec, 0.02f, 0.6f);
        } else {
            addParticle(ParticleTypes.LARGE_SMOKE, 1, player.level, nozzlePos, speedVec, 0.06f, 0.1f);
            if (Math.random() < 0.25) {
                player.level.playSound((Player) player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1, 1);
            }
        }
    }
}
