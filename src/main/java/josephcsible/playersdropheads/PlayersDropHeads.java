package josephcsible.playersdropheads;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = PlayersDropHeads.MODID, name = "PlayersDropHeads", version = "1.0.0", acceptedMinecraftVersions = "[1.12,1.13)")
@Mod.EventBusSubscriber
public class PlayersDropHeads
{
    public static final String MODID = "playersdropheads";

    @Config(modid = MODID)
    public static class Configuration {
        // TODO avoid duplication between @Comment and en_us.lang
        @RangeDouble(min = 0.0D, max = 1.0D)
        @LangKey("config.playersdropheads.normalDropChance")
        @Comment("The chance that a player will drop their head when killed by anything other than a charged creeper or another player.")
        public static double normalDropChance = 1.0D;

        @RangeDouble(min = 0.0D, max = 1.0D)
        @LangKey("config.playersdropheads.pvpDropChance")
        @Comment("The chance that a player will drop their head when killed by another player.")
        public static double pvpDropChance = 1.0D;

        @RangeDouble(min = 0.0D, max = 1.0D)
        @LangKey("config.playersdropheads.chargedCreeperDropChance")
        @Comment("The chance that a player will drop their head when killed by a charged creeper.")
        public static double chargedCreeperDropChance = 1.0D;
    }

    private static void dropSkull(EntityPlayer player) {
        ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
        NBTUtil.writeGameProfile(stack.getOrCreateSubCompound("SkullOwner"), player.getGameProfile());
        player.dropItem(stack, true, false);
    }

    @SubscribeEvent
    public static void onPlayerDrops(PlayerDropsEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        Entity trueSource = event.getSource().getTrueSource();

        if(trueSource instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper)trueSource;
            if(creeper.getPowered() && creeper.ableToCauseSkullDrop()) {
                if(player.world.rand.nextDouble() < Configuration.chargedCreeperDropChance) {
                    creeper.incrementDroppedSkulls();
                    dropSkull(player);
                }
                return;
            }
        }

        if(player.world.rand.nextDouble() < ((trueSource instanceof EntityPlayer && trueSource != player) ? Configuration.pvpDropChance : Configuration.normalDropChance)) {
            dropSkull(player);
        }
    }
}
