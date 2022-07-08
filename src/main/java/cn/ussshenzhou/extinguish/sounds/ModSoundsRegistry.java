package cn.ussshenzhou.extinguish.sounds;

import cn.ussshenzhou.extinguish.Extinguish;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author USS_Shenzhou
 */
public class ModSoundsRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Extinguish.MOD_ID);

    public static final RegistryObject<SoundEvent> CO2_SHOOT = SOUNDS.register("co2_shoot",
            () -> new SoundEvent(new ResourceLocation(Extinguish.MOD_ID, "co2_shoot"))
    );
    public static final RegistryObject<SoundEvent> CO2_START = SOUNDS.register("co2_start",
            () -> new SoundEvent(new ResourceLocation(Extinguish.MOD_ID, "co2_start"))
    );

    public static final RegistryObject<SoundEvent> DRY_SHOOT = SOUNDS.register("dry_shoot",
            () -> new SoundEvent(new ResourceLocation(Extinguish.MOD_ID, "dry_shoot"))
    );
    public static final RegistryObject<SoundEvent> DRY_START = SOUNDS.register("dry_start",
            () -> new SoundEvent(new ResourceLocation(Extinguish.MOD_ID, "dry_start"))
    );
    public static final RegistryObject<SoundEvent> CANNON_SHOOT = SOUNDS.register("cannon_shoot",
            () -> new SoundEvent(new ResourceLocation(Extinguish.MOD_ID, "cannon_shoot"))
    );
}
