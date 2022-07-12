package cn.ussshenzhou.extinguish.blocks;

import cn.ussshenzhou.extinguish.blockentities.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import cn.ussshenzhou.extinguish.Extinguish;
import org.jetbrains.annotations.Nullable;

/**
 * @author USS_Shenzhou
 */
public class ModBlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Extinguish.MOD_ID);

    public static final RegistryObject<ExtinguisherBracketSingle> EXTINGUISHER_BRACKET_SINGLE = BLOCKS.register("extinguisher_bracket_single", ExtinguisherBracketSingle::new);
    public static final RegistryObject<ExtinguisherBracketDouble> EXTINGUISHER_BRACKET_DOUBLE = BLOCKS.register("extinguisher_bracket_double", ExtinguisherBracketDouble::new);
    public static final RegistryObject<ExtinguisherBracketBuiltin> EXTINGUISHER_BRACKET_BUILTIN = BLOCKS.register("extinguisher_bracket_builtin", ExtinguisherBracketBuiltin::new);
    public static final RegistryObject<AutoWaterCannon> AUTO_WATER_CANNON = BLOCKS.register("auto_water_cannon", AutoWaterCannon::new);

    public static final RegistryObject<AutoWaterCannon> AUTO_WATER_CANNON_WHITE = BLOCKS.register("auto_water_cannon_white", () -> new AutoWaterCannon() {
        @Override
        public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
            return pLevel.isClientSide() ?
                    createTickerHelper(pBlockEntityType, ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY_WHITE.get(), AutoWaterCannonEntityWhite::clientTick) :
                    createTickerHelper(pBlockEntityType, ModBlockEntityTypeRegistry.AUTO_WATER_CANNON_BLOCK_ENTITY_WHITE.get(), AutoWaterCannonEntityWhite::serverTick);
        }
    });
    //------demo only------
    public static final RegistryObject<Block> DEMO_OAK_PLANKS = BLOCKS.register("demo_oak_planks", () ->
            new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)) {
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 0;
                }

                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }
            }
    );

    public static final RegistryObject<ExtinguisherBracketSingle> DEMO_EXTINGUISHER_BRACKET_SINGLE = BLOCKS.register("demo_extinguisher_bracket_single", () -> new ExtinguisherBracketSingle() {
        @Override
        public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
            return new ExtinguisherBracketSingleEntity(ModBlockEntityTypeRegistry.DEMO_EXTINGUISHER_BRACKET_SINGLE_BLOCK_ENTITY.get(), pPos, pState) {
                @Override
                public ItemStack removeItem() {
                    syncFromServer(level, this);
                    return getItemStack().copy();
                }
            };
        }
    });
    public static final RegistryObject<ExtinguisherBracketDouble> DEMO_EXTINGUISHER_BRACKET_DOUBLE = BLOCKS.register("demo_extinguisher_bracket_double", () -> new ExtinguisherBracketDouble() {
        @Override
        public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
            return new ExtinguisherBracketDoubleEntity(ModBlockEntityTypeRegistry.DEMO_EXTINGUISHER_BRACKET_DOUBLE_BLOCK_ENTITY.get(), pPos, pState) {
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
    });
    public static final RegistryObject<ExtinguisherBracketBuiltin> DEMO_EXTINGUISHER_BRACKET_BUILTIN = BLOCKS.register("demo_extinguisher_bracket_builtin",()->new ExtinguisherBracketBuiltin(){
        @Override
        public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
            return new ExtinguisherBracketBuiltinEntity(ModBlockEntityTypeRegistry.DEMO_EXTINGUISHER_BRACKET_BUILTIN_BLOCK_ENTITY.get(), pPos,pState){
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
    });
}
