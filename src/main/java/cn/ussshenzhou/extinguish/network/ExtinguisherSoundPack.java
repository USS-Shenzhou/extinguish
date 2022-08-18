package cn.ussshenzhou.extinguish.network;

import cn.ussshenzhou.extinguish.items.AbstractFireExtinguisher;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author USS_Shenzhou
 */
public class ExtinguisherSoundPack {
    private UUID uuid;
    private boolean start;
    private int slot;

    public ExtinguisherSoundPack(UUID uuid, boolean start, int slot) {
        this.uuid = uuid;
        this.start = start;
        this.slot = slot;
    }

    public ExtinguisherSoundPack(FriendlyByteBuf buffer) {
        this.uuid = buffer.readUUID();
        this.start = buffer.readBoolean();
        this.slot = buffer.readInt();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(uuid);
        buffer.writeBoolean(start);
        buffer.writeInt(slot);
    }

    public void handler(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(
                () -> {
                    if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                        serverHandler();
                    } else {
                        clientHandler();
                    }
                }
        );
        context.get().setPacketHandled(true);
    }

    private void serverHandler() {
        MinecraftServer minecraftServer = (MinecraftServer) LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        for (ServerLevel s : minecraftServer.getAllLevels()) {
            Player player = s.getPlayerByUUID(this.uuid);
            if (player != null) {
                AbstractFireExtinguisher.setServerSound(s, player, false, this.slot);
                CompoundTag tag = player.getSlot(this.slot).get().getTag();
                if (tag != null) {
                    tag.putBoolean(AbstractFireExtinguisher.TAG_USING, false);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void clientHandler() {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Player player = level.getPlayerByUUID(this.uuid);
            if (player != null) {
                ItemStack itemStack;
                if (slot == -106) {
                    //off-hand using extinguisher
                    itemStack = player.getItemInHand(InteractionHand.OFF_HAND);
                } else {
                    itemStack = player.getInventory().getItem(0);
                    player.getInventory().setItem(slot, itemStack);
                }
                Item item = itemStack.getItem();
                if (item instanceof AbstractFireExtinguisher abstractFireExtinguisher) {
                    if (start) {
                        abstractFireExtinguisher.playClientSound(level, player);
                    } else {
                        abstractFireExtinguisher.stopClientSound(player);
                        if (itemStack.getTag() != null) {
                            itemStack.getTag().putBoolean(AbstractFireExtinguisher.TAG_USING, false);
                        }
                    }
                }
            }
        }
    }
}
