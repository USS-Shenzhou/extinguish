package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.particles.Co2SmokeParticleOption;
import cn.ussshenzhou.extinguish.sounds.ModSoundsRegistry;
import cn.ussshenzhou.extinguish.sounds.MovableSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;

import java.util.Calendar;

/**
 * @author Tony Yu
 */
public class FireExtinguisherCo2 extends AbstractFireExtinguisher {
    public FireExtinguisherCo2() {
        super(15 * 20);
    }

    @Override
    protected void startSound(Level pLevel, Player pPlayer) {
        if (pLevel.isClientSide) {
            pLevel.playSound(pPlayer,pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSoundsRegistry.CO2_START.get(), SoundSource.PLAYERS,1,1);
            soundInstanceBuffer = new MovableSoundInstance(pPlayer, ModSoundsRegistry.CO2_SHOOT.get(), SoundSource.PLAYERS, 1, 1, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
            Minecraft.getInstance().getSoundManager().play(soundInstanceBuffer);
        }
    }

    @Override
    void shootParticle(LivingEntity player) {
        Vec3 nozzlePos = getNozzlePosInWorld(player, 10, 3.25f);
        Vec3 speedVec = getSpeedVec(player);
        addParticle(new Co2SmokeParticleOption(player.getUUID()), 3, player.level, nozzlePos, speedVec, 0.08f, 0.6f);
    }
}
