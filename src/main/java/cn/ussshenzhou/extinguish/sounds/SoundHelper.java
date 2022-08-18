package cn.ussshenzhou.extinguish.sounds;

import cn.ussshenzhou.extinguish.mixin.SoundEngineAccessor;
import cn.ussshenzhou.extinguish.mixin.SoundManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


/**
 * @author USS_Shenzhou
 */
@OnlyIn(Dist.CLIENT)
public class SoundHelper {

    public static void stopFollowingSound(Player player, ResourceLocation pSoundName, SoundSource pCategory) {
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        SoundEngine soundEngine = ((SoundManagerAccessor) soundManager).getSoundEngine();
        var soundInstanceBySource = ((SoundEngineAccessor) soundEngine).getInstanceBySource();
        for (SoundInstance soundInstance : soundInstanceBySource.get(pCategory)) {
            if (soundInstance instanceof FollowingSoundInstance) {
                if (soundInstance.getLocation().equals(pSoundName) || ((FollowingSoundInstance) soundInstance).getPlayer().getUUID().equals(player.getUUID())) {
                    soundEngine.stop(soundInstance);
                }
            }
        }
    }
}
