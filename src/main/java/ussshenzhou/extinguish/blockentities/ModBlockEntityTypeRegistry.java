package ussshenzhou.extinguish.blockentities;

import com.mojang.datafixers.DSL;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ussshenzhou.extinguish.Extinguish;
import ussshenzhou.extinguish.blocks.ModBlockRegistry;

/**
 * @author Tony Yu
 */
public class ModBlockEntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Extinguish.MOD_ID);

    public static final RegistryObject<BlockEntityType<ExtinguisherBracketEntity>> EXTINGUISHER_BRACKET_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "extinguisher_bracket",
            () -> BlockEntityType.Builder.of(ExtinguisherBracketEntity::new,ModBlockRegistry.EXTINGUISHER_BRACKET.get()).build(DSL.remainderType())
    );
}
