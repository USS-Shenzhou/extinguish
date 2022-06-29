package cn.ussshenzhou.extinguish.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * @author Tony Yu
 */
public class DryPowderParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
    private boolean bouncedOnce = false;
    private UUID shooter;

    protected DryPowderParticle(UUID shooter, ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet pSprites) {
        super(level, x, y, z, vx, vy, vz);
        this.shooter = shooter;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.friction = 0.92f;
        this.hasPhysics = true;
        this.gravity = (float) (0.2f + Math.random() * 0.05f);
        this.lifetime = (int) (15 * 20 + Math.random() * 20 * 5);
        this.setAlpha((float) (0.9 + Math.random() * 0.1));
        float f = 1.0F - (float) (Math.random() * (double) 0.2F);
        this.setColor(f, f, f);
        this.sprites = pSprites;
        this.pickSprite(pSprites);
        this.scale((float) (0.7 + Math.random() * 0.5));
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
            //player interact
            Player player = this.level.getNearestPlayer(this.x, this.y, this.z, 0.7, false);
            if (player != null) {
                this.xd += player.getDeltaMovement().x * Math.random() * 0.3;
                this.zd += player.getDeltaMovement().z * Math.random() * 0.3;
                this.yd += Math.max(player.getDeltaMovement().y * 0.12, 0.12 * Math.sqrt(player.getDeltaMovement().x * player.getDeltaMovement().x + player.getDeltaMovement().z * player.getDeltaMovement().z));
                this.onGround = false;
                this.gravity = 0.08f;
            }

            this.yd -= 0.04D * (double) this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= (double) this.friction;
            this.yd *= (double) this.friction;
            this.zd *= (double) this.friction;

            if (this.onGround) {
                this.xd *= (double) 0.8F;
                this.zd *= (double) 0.8F;
            }
            //Different client have different particle details.
            //Since particle does not exist on server, only shooter's client is the standard position to detect fire.
            //After every 5 ticks, particle has a certain chance to put out fire.
            if (age % 5 == 0 && shooter != null && shooter == Minecraft.getInstance().player.getUUID() && random.nextFloat() < 0.04f) {
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
        //hit XOZ
        if (dy != pY) {
            //only bounce once, then fall and stick to ground.
            if (!bouncedOnce) {
                Vec2 v = ModParticleHelper.spreadOnCollision(random, r2, this.xd, this.zd);
                this.xd = v.x;
                this.yd = -dy * (0.3 + Math.random() * 0.6);
                this.zd = v.y;
                bouncedOnce = true;
                this.gravity = 0.1f;
            } else {
                this.onGround = true;
                this.gravity = 0;
            }
            return;
        }
        //hit YOZ
        float stickChance = 0.3f;
        if (dx != pX) {
            Vec2 v = ModParticleHelper.spreadOnCollision(random, r2, this.yd, this.zd);
            this.yd = v.x;
            this.zd = v.y;
            if (Math.random() < stickChance) {
                this.xd = 0;
                if (Math.random() < 0.5) {
                    this.gravity = 0;
                    this.friction = 0.8f;
                } else {
                    this.gravity = 0.08f;
                }
            } else {
                this.xd = -dy * (0.3 + Math.random() * 0.4);
            }
            bouncedOnce = true;
            return;
        }
        //hit XOY
        if (dz != pZ) {
            Vec2 v = ModParticleHelper.spreadOnCollision(random, r2, this.yd, this.zd);
            this.xd = v.x;
            this.yd = v.y;
            if (Math.random() < stickChance) {
                this.zd = 0;
                if (Math.random() < 0.5) {
                    this.gravity = 0;
                    this.friction = 0.8f;
                } else {
                    this.gravity = 0.08f;
                }
            } else {
                this.zd = -dy * (0.3 + Math.random() * 0.4);
            }
            bouncedOnce = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<DryPowderParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(DryPowderParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            DryPowderParticle d = new DryPowderParticle(pType.getShooter(), pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, sprites);
            d.pickSprite(sprites);
            return d;
        }
    }
}
