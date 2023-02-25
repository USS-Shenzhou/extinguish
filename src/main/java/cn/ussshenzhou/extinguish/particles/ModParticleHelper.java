package cn.ussshenzhou.extinguish.particles;

import cn.ussshenzhou.extinguish.fire.FireHelper;
import cn.ussshenzhou.extinguish.network.PutOutFirePack;
import cn.ussshenzhou.extinguish.network.PutOutFirePackSend;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
        //lose less speed when going forward. lose more speed when going backward.
        float d = (float) Math.sqrt((d1 - a) * (d1 - a) + (d2 - b) * (d2 - b));
        float directionalLoss = 1 - d / (2 * r) * maxDirectionalLoss;
        return new Vec2((float) (a * directionalLoss * Math.random()), (float) (b * directionalLoss * Math.random()));
    }

    public static Vec2 spreadOnCollision(Random random, double r2, double d1, double d2) {
        return spreadOnCollision(random, r2, d1, d2, 0.65f);
    }

    /**
     * @see net.minecraft.world.entity.projectile.ThrownPotion#dowseFire(BlockPos)
     */
    public static void putOut(BlockPos pos) {
        Level level = Minecraft.getInstance().level;
        BlockState blockState = level.getBlockState(pos);
        if (FireHelper.isFire(blockState)) {
            ResourceKey<Level> l = level.dimension();
            ResourceLocation resourceLocation = l.location();
            PutOutFirePackSend.channel.sendToServer(new PutOutFirePack(pos, resourceLocation));
        }
    }
}
