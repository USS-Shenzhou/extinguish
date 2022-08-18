package cn.ussshenzhou.extinguish.mixin;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author USS_Shenzhou
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mixin(SoundManager.class)
public interface SoundManagerAccessor {
    @Accessor
    SoundEngine getSoundEngine();
}
