package cn.ussshenzhou.extinguish.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import cn.ussshenzhou.extinguish.Extinguish;

/**
 * @author Tony Yu
 */
public class ModBlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Extinguish.MOD_ID);

    public static final RegistryObject<ExtinguisherBracketSingle> EXTINGUISHER_BRACKET_SINGLE = BLOCKS.register("extinguisher_bracket_single", ExtinguisherBracketSingle::new);
    public static final RegistryObject<ExtinguisherBracketDouble> EXTINGUISHER_BRACKET_DOUBLE = BLOCKS.register("extinguisher_bracket_double",ExtinguisherBracketDouble::new);
}
