package ussshenzhou.extinguish.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Tony Yu
 */
public class Co2SmokeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
    float scale = 0.15f;

    protected Co2SmokeParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet pSprites) {
        super(level, x, y, z, vx, vy, vz);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.friction = 0.935f;
        this.hasPhysics = true;
        this.gravity = 0;
        this.lifetime = (int) (30 + Math.random() * 10);
        float f = 1.0F - (float) (Math.random() * (double) 0.3F);
        this.setColor(f, f, f);
        this.sprites = pSprites;
        this.setSpriteFromAge(pSprites);
        this.scale(scale);
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
            //this.yd -= 0.04D * (double) this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= (double) this.friction;
            this.yd *= (double) this.friction;
            this.zd *= (double) this.friction;
            this.setSpriteFromAge(this.sprites);

            this.alpha *= 0.94f;
        }
    }


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

        this.scale(1 / scale);
        scale = (float) (Math.sin((float) (this.age) / lifetime * Math.PI / 2) * 3 + 0.15);
        this.scale(scale);
        //hit XOZ
        if (dy != pY) {
            if (dy < 0) {
                this.onGround = true;
            }
            Vec2 v = spreadOnCollision(r2, this.xd, this.zd);
            this.xd = v.x;
            this.yd = 0;
            this.zd = v.y;
            //only collide once
            this.hasPhysics = false;
            //shoot directly down will trigger the other condition.
            return;
        }
        //hit YOZ
        if (dx != pX) {
            Vec2 v = spreadOnCollision(r2, this.yd, this.zd);
            this.xd = 0;
            this.yd = v.x;
            this.zd = v.y;
            this.hasPhysics = false;
            return;
        }
        //hit XOY
        if (dz != pZ) {
            Vec2 v = spreadOnCollision(r2, this.xd, this.yd);
            this.xd = v.x;
            this.yd = v.y;
            this.zd = 0;
            this.hasPhysics = false;
        }
    }

    private Vec2 spreadOnCollision(double r2, double d1, double d2) {
        //lose energy/speed at hitting.
        r2 *= 0.25;
        double r = Math.sqrt(r2);
        double a = Math.random() * r * (random.nextBoolean() ? -1 : 1);
        double b = Math.sqrt(r2 - a * a) * (random.nextBoolean() ? -1 : 1);
        //lose energy/speed at turning to different directions.
        double d = Math.sqrt((d1 - a) * (d1 - a) + (d2 - b) * (d2 - b));
        double maxEkLoss = 0.5;
        double ekLoss = 1 - d / (2 * r) * maxEkLoss;
        return new Vec2((float) (a * ekLoss), (float) (b * ekLoss));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<Co2SmokeParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(Co2SmokeParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new Co2SmokeParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
        }
    }

}
