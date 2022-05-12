package ussshenzhou.extinguish.items;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
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
public abstract class AbstractFireExtinguisher2 extends Item {
    private final int maxTime;
    //private int damageBuffer;
    //private boolean using = false;

    public AbstractFireExtinguisher2(int maxTime) {
        super(new Properties()
                .tab(ModItemGroups.MAIN)
                .stacksTo(1)
                .durability(maxTime)
        );
        this.maxTime = maxTime;
    }

    public boolean isUsing(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("using");
        //return using;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (stack.getDamageValue() < maxTime) {
            LogManager.getLogger().debug("+++++");
            pPlayer.startUsingItem(pUsedHand);
            //using = true;
            stack.getOrCreateTag().putBoolean("using",true);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        //LogManager.getLogger().debug(damageBuffer);
        //saveDamage(pStack);
        //LogManager.getLogger().debug("releaseUsing");
        pStack.getOrCreateTag().putBoolean("using",false);
        super.releaseUsing(pStack,pLevel,pLivingEntity,pTimeCharged);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        /*if (isStackUsing(pStack)) {
            saveDamage(pStack);
        }
        //LogManager.getLogger().debug("finishUsingItem");
        return pStack;*/
        pStack.getOrCreateTag().putBoolean("using",false);
        return super.finishUsingItem(pStack,pLevel,pLivingEntity);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        /*int duration = maxTime - getBufferedDamage(pStack);
        return duration;*/
        LogManager.getLogger().debug(maxTime - pStack.getDamageValue());
        return maxTime - pStack.getDamageValue();
    }


    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxTime;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        //LogManager.getLogger().debug(getUseDuration(stack));
        /*if (player.level.isClientSide) {
            if (getUseDuration(stack) > 0) {
                shootParticle();
                LogManager.getLogger().debug("using!");
            } else {
                player.stopUsingItem();
                saveDamage(stack);
                LogManager.getLogger().debug("stop!!");
            }
        }
        damageBuffer++;*/
        stack.setDamageValue(stack.getDamageValue() + 1);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    /*@Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        if (isStackUsing(pStack)) {
            return damageBuffer > 0 || pStack.isDamaged();
        }
        return super.isBarVisible(pStack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        if (isStackUsing(pStack)) {
            return Math.round(13.0F - getBufferedDamage(pStack) * 13.0F / (float) maxTime);
        }
        return super.getBarWidth(pStack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        if (isStackUsing(pStack)) {
            float f = Math.max(
                    0.0F,
                    ((float) maxTime - getBufferedDamage(pStack)) / maxTime
            );
            return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        }
        return super.getBarColor(pStack);
    }*/

    /*private int getBufferedDamage(ItemStack stack) {
        return Math.min(stack.getDamageValue() + damageBuffer, maxTime);
    }*/

    /*private boolean isStackUsing(ItemStack stack) {
        ItemStack s = null;
        try {
            s = Minecraft.getInstance().player.getUseItem();
        } catch (NullPointerException ignored) {
        }
        return s == stack;
    }*/

    /*private void saveDamage(ItemStack stack) {
        LogManager.getLogger().debug("++++++++" + Math.min(stack.getDamageValue() + damageBuffer, maxTime));
        stack.setDamageValue(
                Math.min(stack.getDamageValue() + damageBuffer, maxTime)
        );
        damageBuffer = 0;
    }*/

    abstract void shootParticle();
}
