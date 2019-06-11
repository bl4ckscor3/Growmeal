package com.builtbroken.sbmgrowmeal;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Growmeal.MODID)
@EventBusSubscriber(bus=Bus.MOD)
public class Growmeal
{
    public static final String MODID = "sbmgrowmeal";
    public static final String NAME = "[SBM] Growmeal";
    public static final String PREFIX = MODID + ":";

    @ObjectHolder(Growmeal.PREFIX + "growmeal")
    public static final Item GROWMEAL = null;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new Item(new Item.Properties().group(ItemGroup.MISC)) {
            @Override
            public ActionResultType onItemUse(ItemUseContext ctx)
            {
                World world = ctx.getWorld();
                BlockPos pos = ctx.getPos();
                PlayerEntity player = ctx.getPlayer();
                Hand hand = null;
                ItemStack stack = ctx.getItem();

                //find out which hand the item is held in to swing the arm later on
                if(player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == GROWMEAL)
                    hand = Hand.MAIN_HAND;
                else if(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem() == GROWMEAL)
                    hand = Hand.OFF_HAND;

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
                                stack.shrink(1);

                            return ActionResultType.SUCCESS;
                        }
                    }

                    player.swingArm(hand); //swings according to this method's return value
                }

                return ActionResultType.PASS;
            }
        }.setRegistryName(new ResourceLocation(Growmeal.MODID, "growmeal")));
    }
}
