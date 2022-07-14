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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

/**
 * @author USS_Shenzhou
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract ParticleStatus calculateParticleLevel(boolean pDecreased);


    /**
     * Change the max distance of particle spawn (32->64).
     * This would effect:
     * @see AutoWaterCannonEntity#shootWater()
     */
    @Inject(method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void mixinAddParticleInternal(ParticleOptions pOptions, boolean pForce, boolean pDecreased, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, CallbackInfoReturnable<Particle> cir) {
        Camera camera = this.minecraft.gameRenderer.getMainCamera();
        if (this.minecraft != null && camera.isInitialized() && this.minecraft.particleEngine != null) {
            ParticleStatus particlestatus = this.calculateParticleLevel(pDecreased);
            if (pForce) {
                cir.setReturnValue(this.minecraft.particleEngine.createParticle(pOptions, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed));
            } else if (camera.getPosition().distanceToSqr(pX, pY, pZ) > 64 * 64) {
                cir.setReturnValue(null);
            } else {
                cir.setReturnValue(particlestatus == ParticleStatus.MINIMAL ? null : this.minecraft.particleEngine.createParticle(pOptions, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed));
            }
        } else {
            cir.setReturnValue(null);
        }
    }

}
