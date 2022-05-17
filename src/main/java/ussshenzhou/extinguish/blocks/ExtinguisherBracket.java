package ussshenzhou.extinguish.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;

/**
 * @author Tony Yu
 */
public class ExtinguisherBracket extends Block {
    public static DirectionProperty direction = BlockStateProperties.FACING;

    public ExtinguisherBracket() {
        super(
                Properties.of(Material.METAL)
                        .noOcclusion()
                        .strength(1, 5)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(direction);
        super.createBlockStateDefinition(pBuilder);
    }
}
