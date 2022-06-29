package cn.ussshenzhou.extinguish.util;

import cn.ussshenzhou.extinguish.items.ModItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import cn.ussshenzhou.extinguish.Extinguish;

/**
 * @author USS_Shenzhou
 */
public class ModItemGroups {
    /**
     * ItemGroup sounds way better.
     * MCP RIP.
     */
    public static CreativeModeTab MAIN = new CreativeModeTab(Extinguish.MOD_ID + ":main") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItemRegistry.FIRE_EXTINGUISHER_CO2.get());
        }
    };
}
