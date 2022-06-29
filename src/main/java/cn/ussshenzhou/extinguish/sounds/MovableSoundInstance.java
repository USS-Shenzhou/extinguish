package cn.ussshenzhou.extinguish.sounds;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;


/**
 * @author USS_Shenzhou
 */
public class MovableSoundInstance extends SimpleSoundInstance implements TickableSoundInstance {
    private Player player;

    public MovableSoundInstance(Player player, SoundEvent pSoundEvent, SoundSource pSource, float pVolume, float pPitch, double pX, double pY, double pZ) {
        super(pSoundEvent, pSource, pVolume, pPitch, pX, pY, pZ);
        this.player = player;
    }

    @Deprecated
    public void setPos(Vec3 vec3){
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
    }
    @Deprecated
    public void setPos(double x,double y,double z){
        this.x = x;
        this.y = y;
        this.z = z;
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
}
