package cn.ussshenzhou.extinguish.items;

import cn.ussshenzhou.extinguish.blocks.ModBlockRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import cn.ussshenzhou.extinguish.Extinguish;
import cn.ussshenzhou.extinguish.util.ModItemGroups;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author USS_Shenzhou
 */
public class ModItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Extinguish.MOD_ID);
    public static final RegistryObject<Item> FIRE_EXTINGUISHER_CO2 = ITEMS.register("extinguisher_co2", FireExtinguisherCo2::new);
    public static final RegistryObject<Item> FIRE_EXTINGUISHER_WATER = ITEMS.register("extinguisher_water", FireExtinguisherWater::new);
    public static final RegistryObject<Item> FIRE_EXTINGUISHER_DRY = ITEMS.register("extinguisher_dry", FireExtinguisherDry::new);

    public static final RegistryObject<Item> EXTINGUISHER_BRACKET_SINGLE = ITEMS.register("extinguisher_bracket_single", () ->
            new BlockItem(ModBlockRegistry.EXTINGUISHER_BRACKET_SINGLE.get(), new Item.Properties().tab(ModItemGroups.MAIN))
    );

    public static final RegistryObject<Item> EXTINGUISHER_BRACKET_DOUBLE = ITEMS.register("extinguisher_bracket_double", () ->
            new BlockItem(ModBlockRegistry.EXTINGUISHER_BRACKET_DOUBLE.get(), new Item.Properties().tab(ModItemGroups.MAIN))
    );
    public static final RegistryObject<Item> EXTINGUISHER_BRACKET_BUILTIN = ITEMS.register("extinguisher_bracket_builtin", () ->
            new BlockItem(ModBlockRegistry.EXTINGUISHER_BRACKET_BUILTIN.get(), new Item.Properties().tab(ModItemGroups.MAIN)) {
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                    TranslatableComponent translatableComponent1 = new TranslatableComponent("item.extinguish.tip.extinguisher_bracket_builtin1");
                    translatableComponent1.setStyle(this.getDescription().getStyle().withColor(0xaa0000));
                    pTooltip.add(translatableComponent1);
                    TranslatableComponent translatableComponent2 = new TranslatableComponent("item.extinguish.tip.extinguisher_bracket_builtin2");
                    translatableComponent2.setStyle(this.getDescription().getStyle().withColor(0xaa0000));
                    pTooltip.add(translatableComponent2);
                }
            }
    );
    public static final RegistryObject<Item> AUTO_WATER_CANNON = ITEMS.register("auto_water_cannon", () ->
            new BlockItem(ModBlockRegistry.AUTO_WATER_CANNON.get(), new Item.Properties().tab(ModItemGroups.MAIN))
    );

    public static final RegistryObject<Item> AUTO_WATER_CANNON_WHITE = ITEMS.register("auto_water_cannon_white", () ->
            new BlockItem(ModBlockRegistry.AUTO_WATER_CANNON_WHITE.get(), new Item.Properties().tab(ModItemGroups.MAIN))
    );

    public static final RegistryObject<Item> DEMO_ASH = ITEMS.register("demo_ash", () ->
            new Item(new Item.Properties().tab(ModItemGroups.MAIN)) {
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
                    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
                    TranslatableComponent translatableComponent1 = new TranslatableComponent("item.extinguish.tip.demo_ash");
                    translatableComponent1.setStyle(this.getDescription().getStyle().withColor(0xaa0000));
                    pTooltipComponents.add(translatableComponent1);
                    TranslatableComponent translatableComponent2 = new TranslatableComponent("item.extinguish.tip.demo_ash.teacon");
                    translatableComponent2.setStyle(this.getDescription().getStyle().withColor(0xdf292a));
                    pTooltipComponents.add(translatableComponent2);
                }
            }
    );
    public static final RegistryObject<Item> EMPTY_EXTINGUISHER_JAR = ITEMS.register("empty_extinguisher_jar", () ->
            new Item(new Item.Properties()
                    .tab(ModItemGroups.MAIN)
            )
    );
}
