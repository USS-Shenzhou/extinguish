package ussshenzhou.extinguish.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ussshenzhou.extinguish.Extinguish;

/**
 * @author Tony Yu
 */
public class ModParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Extinguish.MOD_ID);

    public static final RegistryObject<ParticleType<Co2SmokeParticleOption>> CO2_SMOKE_PARTICLE = PARTICLE_TYPES.register("co2_smoke", Co2SmokeParticleType::new);
    public static final RegistryObject<ParticleType<DryPowderParticleOption>> DRY_POWDER_PARTICLE = PARTICLE_TYPES.register("dry_powder", DryPowderParticleType::new);
}
