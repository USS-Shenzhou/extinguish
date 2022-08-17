package cn.ussshenzhou.extinguish.mixin;

import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author USS_Shenzhou
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract ParticleStatus calculateParticleLevel(boolean pDecreased);


    /**
     * Change the max distance of particle spawn (32->64).
     * This would effect:
     *
     * @see AutoWaterCannonEntity#shootWater()
     */
    @ModifyConstant(method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", constant = @Constant(doubleValue = 1024d))
    private double particleDistance(double old) {
        return 64 * 64;
    }
}
