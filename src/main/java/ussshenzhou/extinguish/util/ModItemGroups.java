package ussshenzhou.extinguish.util;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import ussshenzhou.extinguish.Extinguish;
import ussshenzhou.extinguish.items.ModItemRegistry;

/**
 * @author Tony Yu
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
