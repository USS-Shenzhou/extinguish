package ussshenzhou.extinguish.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import ussshenzhou.extinguish.util.ModItemGroups;

/**
 * @author Tony Yu
 */
public abstract class AbstractFireExtinguisher extends Item {
    private final int maxTime;
    //private int damageBuffer;
    //private boolean using = false;

    public AbstractFireExtinguisher(int maxTime) {
        super(new Properties()
                .tab(ModItemGroups.MAIN)
                .stacksTo(1)
                .durability(maxTime)
        );
        this.maxTime = maxTime;
    }

    public boolean isUsing(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("using");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (stack.getDamageValue() < maxTime) {
            LogManager.getLogger().debug("+++++");
            pPlayer.startUsingItem(pUsedHand);
            stack.getOrCreateTag().putBoolean("using",true);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        pStack.getOrCreateTag().putBoolean("using",false);
        super.releaseUsing(pStack,pLevel,pLivingEntity,pTimeCharged);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        pStack.getOrCreateTag().putBoolean("using",false);
        return super.finishUsingItem(pStack,pLevel,pLivingEntity);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        LogManager.getLogger().debug(maxTime - pStack.getDamageValue());
        return maxTime - pStack.getDamageValue();
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxTime;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        stack.setDamageValue(stack.getDamageValue() + 1);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    abstract void shootParticle();
}
