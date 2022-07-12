package cn.ussshenzhou.extinguish.blockentities;

import cn.ussshenzhou.extinguish.Extinguish;
import cn.ussshenzhou.extinguish.blocks.ModBlockRegistry;
import com.mojang.datafixers.DSL;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author USS_Shenzhou
 */
public class ModBlockEntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Extinguish.MOD_ID);

    public static final RegistryObject<BlockEntityType<ExtinguisherBracketSingleEntity>> EXTINGUISHER_BRACKET_SINGLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "extinguisher_bracket_single",
            () -> BlockEntityType.Builder.of(ExtinguisherBracketSingleEntity::new, ModBlockRegistry.EXTINGUISHER_BRACKET_SINGLE.get()).build(DSL.remainderType())
    );
    public static final RegistryObject<BlockEntityType<ExtinguisherBracketDoubleEntity>> EXTINGUISHER_BRACKET_DOUBLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "extinguisher_bracket_double",
            () -> BlockEntityType.Builder.of(ExtinguisherBracketDoubleEntity::new, ModBlockRegistry.EXTINGUISHER_BRACKET_DOUBLE.get()).build(DSL.remainderType())
    );
    public static final RegistryObject<BlockEntityType<ExtinguisherBracketBuiltinEntity>> EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "extinguisher_bracket_builtin",
            () -> BlockEntityType.Builder.of(ExtinguisherBracketBuiltinEntity::new, ModBlockRegistry.EXTINGUISHER_BRACKET_BUILTIN.get()).build(DSL.remainderType())
    );
    public static final RegistryObject<BlockEntityType<AutoWaterCannonEntity>> AUTO_WATER_CANNON_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "auto_water_cannon",
            () -> BlockEntityType.Builder.of(AutoWaterCannonEntity::new, ModBlockRegistry.AUTO_WATER_CANNON.get()).build(DSL.remainderType())
    );
    public static final RegistryObject<BlockEntityType<AutoWaterCannonEntityWhite>> AUTO_WATER_CANNON_BLOCK_ENTITY_WHITE = BLOCK_ENTITY_TYPES.register(
            "auto_water_cannon_white",
            () -> BlockEntityType.Builder.of(AutoWaterCannonEntityWhite::new, ModBlockRegistry.AUTO_WATER_CANNON_WHITE.get()).build(DSL.remainderType())
    );
    //------demo only------
    public static final RegistryObject<BlockEntityType<ExtinguisherBracketSingleEntity>> DEMO_EXTINGUISHER_BRACKET_SINGLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "demo_extinguisher_bracket_single",
            () -> BlockEntityType.Builder.of(ModBlockEntityTypeRegistry::createDemoExtinguisherBracketSingleEntity,
                    ModBlockRegistry.DEMO_EXTINGUISHER_BRACKET_SINGLE.get()).build(DSL.remainderType())
    );

    private static ExtinguisherBracketSingleEntity createDemoExtinguisherBracketSingleEntity(BlockPos pos, BlockState state) {
        return new ExtinguisherBracketSingleEntity(DEMO_EXTINGUISHER_BRACKET_SINGLE_BLOCK_ENTITY.get(), pos, state) {
            @Override
            public ItemStack removeItem() {
                syncFromServer(level, this);
                return getItemStack().copy();
            }
        };
    }

    public static final RegistryObject<BlockEntityType<ExtinguisherBracketDoubleEntity>> DEMO_EXTINGUISHER_BRACKET_DOUBLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "demo_extinguisher_bracket_double",
            () -> BlockEntityType.Builder.of(ModBlockEntityTypeRegistry::createDemoExtinguisherBracketDoubleEntity,
                    ModBlockRegistry.DEMO_EXTINGUISHER_BRACKET_DOUBLE.get()).build(DSL.remainderType())
    );

    private static ExtinguisherBracketDoubleEntity createDemoExtinguisherBracketDoubleEntity(BlockPos pos, BlockState state) {
        return new ExtinguisherBracketDoubleEntity(DEMO_EXTINGUISHER_BRACKET_DOUBLE_BLOCK_ENTITY.get(), pos, state) {
            @Override
            public ItemStack removeItem() {
                ItemStack i;
                if (!itemStacks[0].isEmpty()) {
                    i = itemStacks[0].copy();
                } else {
                    i = itemStacks[1].copy();
                }
                return i;
            }
        };
    }
    public static final RegistryObject<BlockEntityType<ExtinguisherBracketBuiltinEntity>> DEMO_EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
      "demo_extinguisher_bracket_builtin",
            ()->BlockEntityType.Builder.of(ModBlockEntityTypeRegistry::createExtinguisherBracketBuiltinEntity,
                    ModBlockRegistry.DEMO_EXTINGUISHER_BRACKET_BUILTIN.get()).build(DSL.remainderType())
    );

    private static ExtinguisherBracketBuiltinEntity createExtinguisherBracketBuiltinEntity(BlockPos pos, BlockState state){
        return new ExtinguisherBracketBuiltinEntity(DEMO_EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY.get(),pos,state){
            @Override
            public ItemStack removeItem() {
                ItemStack i;
                if (!itemStacks[0].isEmpty()) {
                    i = itemStacks[0].copy();
                } else {
                    i = itemStacks[1].copy();
                }
                return i;
            }
        };
    }
}
