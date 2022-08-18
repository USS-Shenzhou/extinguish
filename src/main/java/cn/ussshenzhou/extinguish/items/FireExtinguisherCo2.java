package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.particles.Co2SmokeParticleOption;
import cn.ussshenzhou.extinguish.sounds.FollowingSoundInstance;
import cn.ussshenzhou.extinguish.sounds.ModSoundsRegistry;
//import cn.ussshenzhou.extinguish.sounds.MovableSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author USS_Shenzhou
 */
public class FireExtinguisherCo2 extends AbstractFireExtinguisher {
    public FireExtinguisherCo2() {
        super(15 * 20);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void playClientSound(Level pLevel, Player pPlayer) {
        if (pLevel.isClientSide) {
            pLevel.playLocalSound(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSoundsRegistry.CO2_START.get(), SoundSource.PLAYERS, 1, 1, false);
            FollowingSoundInstance f = new FollowingSoundInstance(pPlayer, getShootSound(), SoundSource.PLAYERS, 1, 1, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
            Minecraft.getInstance().getSoundManager().play(f);
        }
    }

    @Override
    public SoundEvent getShootSound() {
        return ModSoundsRegistry.CO2_SHOOT.get();
    }

    @Override
    protected void interactWithOtherEntity(LivingEntity entity) {
        entity.setAirSupply(entity.getAirSupply() - 7);
        if (interactCounter % 20 == 0 && entity.getAirSupply() <= 0) {
            entity.hurt(DamageSource.MAGIC, 1);
        }
    }

    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 10, 3.25f);
        Vec3 speedVec = getSpeedVec(player);
        addParticle(new Co2SmokeParticleOption(player.getUUID()), 3, player.level, nozzlePos, speedVec, 0.08f, 0.6f);
    }
}
