package cn.ussshenzhou.extinguish.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Tony Yu
 */
public class WaterSpoutParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
    private boolean fade = false;
    private UUID shooter;

    protected WaterSpoutParticle(UUID shooter, ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet pSprites) {
        super(level, x, y, z, vx, vy, vz);
        this.shooter = shooter;
        this.sprites = pSprites;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.friction = 0.92f;
        this.hasPhysics = true;
        this.gravity = (float) (0.18f + Math.random() * 0.1f);
        this.lifetime = (int) (15 * 20 + Math.random() * 20 * 5);
        this.setAlpha((float) (0.8 + Math.random() * 0.2));
        float f = 1.0F - (float) (Math.random() * (double) 0.2F);
        this.setColor(f, f, f);
        this.pickSprite(pSprites);
        this.scale((float) (0.4 + Math.random() * 0.3));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.04D * (double) this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= (double) this.friction;
            this.yd *= (double) this.friction;
            this.zd *= (double) this.friction;
            this.setSpriteFromAge(this.sprites);
            if (this.fade) {
                this.alpha *= 0.995f;
            }
            //Different client have different particle details.
            //Since particle does not exist on server, only shooter's client is the standard position to detect fire.
            //After every 5 ticks, particle has a certain chance to put out fire.
            if (age % 5 == 0 && shooter.equals(Minecraft.getInstance().player.getUUID()) && random.nextFloat() < 0.06f) {
                ModParticleHelper.putOut(new BlockPos(x, y, z));
            }
        }
    }

    @SuppressWarnings({"", "AlibabaAvoidDoubleOrFloatEqualCompare"})
    @Override
    public void move(double pX, double pY, double pZ) {
        double dx = pX;
        double dy = pY;
        double dz = pZ;
        double r2 = pX * pX + pY * pY + pZ * pZ;
        if (this.hasPhysics && (pX != 0.0D || pY != 0.0D || pZ != 0.0D) && r2 < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 vec3 = Entity.collideBoundingBox((Entity) null, new Vec3(pX, pY, pZ), this.getBoundingBox(), this.level, List.of());
            pX = vec3.x;
            pY = vec3.y;
            pZ = vec3.z;
        }
        if (pX != 0.0D || pY != 0.0D || pZ != 0.0D) {
            this.setBoundingBox(this.getBoundingBox().move(pX, pY, pZ));
            this.setLocationFromBoundingbox();
        }
        float stickChance = 0.15f;
        //hit XOZ
        if (dy != pY) {
            Vec2 v = ModParticleHelper.spreadOnCollision(random, r2, this.xd, this.zd, 0.5f);
            this.xd = v.x;
            this.yd = 0;
            this.zd = v.y;
            if (dy <= 0) {
                //hit floor
                this.onGround = true;
                this.fade = true;
                this.friction = 0.91f;
                this.hasPhysics = false;
                this.gravity = 0;
            } else {
                //hit ceiling
                if (Math.random() < stickChance) {
                    this.gravity = 0;
                    this.fade = true;
                } else {
                    this.gravity = 0.1f;
                }
            }
            //shoot directly down will trigger the other conditions.
            return;
        }
        //hit YOZ
        if (dx != pX) {
            Vec2 v = ModParticleHelper.spreadOnCollision(random, r2, this.yd, this.zd, 0.5f);
            this.xd = 0;
            this.yd = v.x;
            this.zd = v.y;
            this.friction = 0.87f;
            if (Math.random() < stickChance) {
                this.gravity = 0;
                this.fade = true;
            } else {
                this.gravity = 0.06f;
            }
            return;
        }
        //hit XOY
        if (dz != pZ) {
            Vec2 v = ModParticleHelper.spreadOnCollision(random, r2, this.xd, this.yd, 0.5f);
            this.xd = v.x;
            this.yd = v.y;
            this.zd = 0;
            this.friction = 0.87f;
            if (Math.random() < stickChance) {
                this.gravity = 0;
                this.fade = true;
            } else {
                this.gravity = 0.06f;
            }
            return;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<WaterSpoutParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(WaterSpoutParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new WaterSpoutParticle(pType.getShooter(), pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
        }
    }
}
