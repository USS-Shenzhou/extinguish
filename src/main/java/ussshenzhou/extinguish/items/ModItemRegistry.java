package ussshenzhou.extinguish.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ussshenzhou.extinguish.Extinguish;

/**
 * @author Tony Yu
 */
public class ModItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Extinguish.MOD_ID);

    public static final RegistryObject<Item> FIRE_EXTINGUISHER_CO2 = ITEMS.register("extinguisher_co2",FireExtinguisherCo2::new);
    public static final RegistryObject<Item> FIRE_EXTINGUISHER_WATER = ITEMS.register("extinguisher_water",FireExtinguisherWater::new);
    public static final RegistryObject<Item> FIRE_EXTINGUISHER_DRY = ITEMS.register("extinguisher_dry",FireExtinguisherDry::new);
}
