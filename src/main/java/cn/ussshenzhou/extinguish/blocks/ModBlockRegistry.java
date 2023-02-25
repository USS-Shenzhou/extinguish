package cn.ussshenzhou.extinguish.blocks;

import cn.ussshenzhou.extinguish.Extinguish;
import cn.ussshenzhou.extinguish.blockentities.AutoWaterCannonEntityWhite;
import cn.ussshenzhou.extinguish.blockentities.ModBlockEntityTypeRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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
}
