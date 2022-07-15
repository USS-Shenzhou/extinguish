package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.sounds.ModSoundsRegistry;
import cn.ussshenzhou.extinguish.sounds.MovableSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import cn.ussshenzhou.extinguish.particles.DryPowderParticleOption;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author USS_Shenzhou
 */
public class FireExtinguisherDry extends AbstractFireExtinguisher {

    public FireExtinguisherDry() {
        super(360);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void startSound(Level pLevel, Player pPlayer) {
        if (pLevel.isClientSide) {
            pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSoundsRegistry.DRY_START.get(), SoundSource.PLAYERS, 1.1f, 1.05f);
            soundInstanceBuffer = new MovableSoundInstance(pPlayer, ModSoundsRegistry.DRY_SHOOT.get(), SoundSource.PLAYERS, 1.1f, 1.05f, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
            Minecraft.getInstance().getSoundManager().play(soundInstanceBuffer);
        }
    }

    @Override
    protected void interactWithOtherEntity(Entity entity) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, interactCounter, 0));
            if (interactCounter % 20 == 0) {
                entity.hurt(DamageSource.MAGIC, 0.5f);
            }
        }
    }

    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 13);
        Vec3 speedVec = getSpeedVec(player);
        addParticle(new DryPowderParticleOption(player.getUUID()), 3, player.level, nozzlePos, speedVec, 0.065f, 0.7f);
    }
}
