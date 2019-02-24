package com.builtbroken.sbmgrowmeal;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid=Growmeal.MODID, name=Growmeal.NAME, version=Growmeal.VERSION, acceptedMinecraftVersions=Growmeal.MC_VERSION)
@EventBusSubscriber
public class Growmeal
{
    public static final String MODID = "sbmgrowmeal";
    public static final String NAME = "[SBM] Growmeal";
    public static final String VERSION = ""; //TODO
    public static final String MC_VERSION = "1.12";
    public static final String PREFIX = MODID + ":";

    @ObjectHolder(Growmeal.PREFIX + "growmeal")
    public static final Item GROWMEAL = null;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new Item() {
            @Override
            public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
            {
                //world.getBlockState(pos) is never saved as the state will not update otherwhise after applying the meal
                if(world.getBlockState(pos).getBlock() instanceof IGrowable)
                {
                    if(!world.isRemote)
                    {
                        boolean hasGrown = false;

                        for(int i = 0; i < 1000; i++) //try a thousand times to not have an infinite loop with a growable that can grow infinite times
                        {
                            Block growable = world.getBlockState(pos).getBlock();

                            if(growable instanceof IGrowable && ((IGrowable)growable).canGrow(world, pos, world.getBlockState(pos), world.isRemote))
                            {
                                ((IGrowable)growable).grow(world, world.rand, pos, world.getBlockState(pos));
                                hasGrown = true;
                            }
                            else break; //no need to try even more when the growable can't grow
                        }

                        if(hasGrown) //make sure that the growable has grown before removing an item from the player
                        {
                            world.playEvent(2005, pos, 0); //particles

                            if(!player.isCreative())
                                player.getHeldItem(hand).shrink(1);

                            return EnumActionResult.SUCCESS;
                        }
                    }

                    player.swingArm(hand); //swings according to this method's return value
                }

                return EnumActionResult.PASS;
            }
        }.setRegistryName(new ResourceLocation(Growmeal.MODID, "growmeal")).setCreativeTab(CreativeTabs.MISC).setTranslationKey(Growmeal.PREFIX + "growmeal"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(GROWMEAL, 0, new ModelResourceLocation(new ResourceLocation(Growmeal.MODID, "growmeal"), "inventory"));
    }
}
