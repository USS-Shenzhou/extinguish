package cn.ussshenzhou.extinguish.sounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Optional;

/**
 * @author USS_Shenzhou
 */
public class ClientSoundManager {
    private static LinkedHashSet<FollowingSoundInstance> followingSoundInstances = new LinkedHashSet<>();

    public static void addSound(FollowingSoundInstance f) {
        followingSoundInstances.add(f);
    }

    public static void removeSound(FollowingSoundInstance f) {
        followingSoundInstances.remove(f);
    }

    public static Optional<FollowingSoundInstance> getFollowingSoundInstance(Player player, SoundEvent s) {
        for (FollowingSoundInstance f : followingSoundInstances) {
            if (f.getPlayer().equals(player) && f.getSound().getLocation().equals(s.getLocation())) {
                return Optional.of(f);
            }
        }
        return Optional.empty();
    }
}
