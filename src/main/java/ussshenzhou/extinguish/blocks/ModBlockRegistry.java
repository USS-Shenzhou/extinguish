package ussshenzhou.extinguish.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ussshenzhou.extinguish.Extinguish;

/**
 * @author Tony Yu
 */
public class ModBlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Extinguish.MOD_ID);

    public static final RegistryObject<ExtinguisherBracket> EXTINGUISHER_BRACKET = BLOCKS.register("extinguisher_bracket", ExtinguisherBracket::new);
}
