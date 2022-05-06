package ussshenzhou.extinguish.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ussshenzhou.extinguish.Extinguish;

/**
 * @author Tony Yu
 */
public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Extinguish.MOD_ID);

    //public static final RegistryObject<Item> TEST = ITEMS.register("test",()->new Item(new Item.Properties()));
    //public static Item i = TEST.get();
}
