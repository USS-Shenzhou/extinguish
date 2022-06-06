package cn.ussshenzhou.extinguish.particles;

import net.minecraft.world.phys.Vec2;

import java.util.Random;

/**
 * @author USS_Shenzhou
 */
public class ModParticleHelper {
    public static Vec2 spreadOnCollision(Random random, double r2, double d1, double d2, float maxDirectionalLoss) {
        //generalLoss controls radius of spread circle.
        float generalLoss = 0.7f;
        r2 *= 1 - generalLoss;
        float r = (float) Math.sqrt(r2);
        float a = (float) Math.random() * r * (random.nextBoolean() ? -1 : 1);
        float b = (float) Math.sqrt(r2 - a * a) * (random.nextBoolean() ? -1 : 1);
        //lose energy/speed when bouncing to different directions.
        //lose less speed when going forward. lose less speed when going backward.
        float d = (float) Math.sqrt((d1 - a) * (d1 - a) + (d2 - b) * (d2 - b));
        float directionalLoss = 1 - d / (2 * r) * maxDirectionalLoss;
        return new Vec2((float) (a * directionalLoss * Math.random()), (float) (b * directionalLoss * Math.random()));
    }

    public static Vec2 spreadOnCollision(Random random, double r2, double d1, double d2) {
        return spreadOnCollision(random, r2, d1, d2, 0.65f);
    }
}
