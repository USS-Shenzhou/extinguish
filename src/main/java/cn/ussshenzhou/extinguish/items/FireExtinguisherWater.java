package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.particles.WaterSpoutParticleOption;
import cn.ussshenzhou.extinguish.sounds.ModSoundsRegistry;
import cn.ussshenzhou.extinguish.sounds.MovableSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @author Tony Yu
 */
public class FireExtinguisherWater extends AbstractFireExtinguisher {
    public FireExtinguisherWater() {
        super(12 * 20);
    }

    @Override
    protected void interactWithBlaze(ItemStack stack, Player player, Blaze blaze) {
        if (player.level.dimension() != Level.NETHER) {
            super.interactWithBlaze(stack, player, blaze);
        }
    }

    @Override
    protected void startSound(Level pLevel, Player pPlayer) {
        if (pLevel.isClientSide) {
            pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSoundsRegistry.DRY_START.get(), SoundSource.PLAYERS, 0.8f, 0.9f);
            if (pPlayer.level.dimension() != Level.NETHER) {
                soundInstanceBuffer = new MovableSoundInstance(pPlayer, ModSoundsRegistry.DRY_SHOOT.get(), SoundSource.PLAYERS, 0.8f, 0.9f, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
            } else {
                soundInstanceBuffer = new MovableSoundInstance(pPlayer, ModSoundsRegistry.DRY_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 0.9f, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
            }
            Minecraft.getInstance().getSoundManager().play(soundInstanceBuffer);
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
