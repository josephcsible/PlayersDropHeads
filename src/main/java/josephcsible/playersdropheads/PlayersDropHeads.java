package josephcsible.playersdropheads;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import org.apache.logging.log4j.Logger;

@Mod(modid = PlayersDropHeads.MODID, name = PlayersDropHeads.NAME, version = PlayersDropHeads.VERSION)
@Mod.EventBusSubscriber
public class PlayersDropHeads
{
    public static final String MODID = "playersdropheads";
    public static final String NAME = "PlayersDropHeads";
    public static final String VERSION = "1.0.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private static void dropSkull(EntityPlayer player) {
    	ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
    	NBTUtil.writeGameProfile(stack.getOrCreateSubCompound("SkullOwner"), player.getGameProfile());
    	player.dropItem(stack, true, false);
    }

    @SubscribeEvent
    public static void onPlayerDrop(PlayerDropsEvent event) {
    	DamageSource cause = event.getSource();
        if (cause.getTrueSource() instanceof EntityCreeper)
        {
            EntityCreeper creeper = (EntityCreeper)cause.getTrueSource();

            if (creeper.getPowered() && creeper.ableToCauseSkullDrop())
            {
            	if(true) { // TODO
	                creeper.incrementDroppedSkulls();
	                dropSkull(event.getEntityPlayer());
            	}
                return;
            }
        }

        if(true) {
        	dropSkull(event.getEntityPlayer());
        }
    }
}
