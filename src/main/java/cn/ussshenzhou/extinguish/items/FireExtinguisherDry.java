package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.sounds.ModSoundsRegistry;
import cn.ussshenzhou.extinguish.sounds.MovableSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import cn.ussshenzhou.extinguish.particles.DryPowderParticleOption;

/**
 * @author Tony Yu
 */
public class FireExtinguisherDry extends AbstractFireExtinguisher {

    public FireExtinguisherDry() {
        super(360);
    }

    @Override
    protected void startSound(Level pLevel, Player pPlayer) {
        if (pLevel.isClientSide) {
            pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSoundsRegistry.DRY_START.get(), SoundSource.PLAYERS, 1.1f, 1.05f);
            soundInstanceBuffer = new MovableSoundInstance(pPlayer, ModSoundsRegistry.DRY_SHOOT.get(), SoundSource.PLAYERS, 1.1f, 1.05f, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
            Minecraft.getInstance().getSoundManager().play(soundInstanceBuffer);
        }
    }

    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 13);
        Vec3 speedVec = getSpeedVec(player);
        addParticle(new DryPowderParticleOption(player.getUUID()), 3, player.level, nozzlePos, speedVec, 0.065f, 0.7f);
    }
}
