package cn.ussshenzhou.extinguish;

import cn.ussshenzhou.extinguish.blockentities.ModBlockEntityTypeRegistry;
import cn.ussshenzhou.extinguish.blocks.ModBlockRegistry;
import cn.ussshenzhou.extinguish.items.ModItemRegistry;
import cn.ussshenzhou.extinguish.particles.ModParticleRegistry;
import cn.ussshenzhou.extinguish.sounds.ModSoundsRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("extinguish")
public class Extinguish {

    public static final String MOD_ID = "extinguish";
    public Extinguish() {

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItemRegistry.ITEMS.register(modBus);
        ModParticleRegistry.PARTICLE_TYPES.register(modBus);
        ModBlockRegistry.BLOCKS.register(modBus);
        ModBlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        ModSoundsRegistry.SOUNDS.register(modBus);
    }
}
