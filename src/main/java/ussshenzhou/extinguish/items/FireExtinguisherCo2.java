package ussshenzhou.extinguish.items;

import net.minecraft.world.item.Item;
import ussshenzhou.extinguish.util.ItemGroups;

/**
 * @author Tony Yu
 */
public class FireExtinguisherCo2 extends Item {
    public FireExtinguisherCo2() {
        super(new Properties()
                .tab(ItemGroups.MAIN)
        );
        this.setRegistryName("fire_extinguisher_co2");
    }
}
