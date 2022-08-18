package cn.ussshenzhou.extinguish.sounds;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;


/**
 * @author USS_Shenzhou
 */
public class FollowingSoundInstance extends SimpleSoundInstance implements TickableSoundInstance {
    private Player player;

    public FollowingSoundInstance(Player player, SoundEvent pSoundEvent, SoundSource pSource, float pVolume, float pPitch, double pX, double pY, double pZ) {
        super(pSoundEvent, pSource, pVolume, pPitch, pX, pY, pZ);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    @Override
    public void tick() {
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public int hashCode() {
        return player.hashCode() + sound.getLocation().hashCode() + (int) (volume * 1000 + pitch * 1000);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
