package ussshenzhou.extinguish.util;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ussshenzhou.extinguish.Extinguish;

/**
 * @author Tony Yu
 */
public class ItemGroups {
    /**
     * ItemGroup sounds way better.
     * MCP RIP.
     */
    public static CreativeModeTab MAIN = new CreativeModeTab(Extinguish.MOD_ID + ":main") {
        @Override
        public ItemStack makeIcon() {
            return null;
        }
    };
}
