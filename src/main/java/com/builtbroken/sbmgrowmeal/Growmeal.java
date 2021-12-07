package com.builtbroken.sbmgrowmeal;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Growmeal.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public class Growmeal
{
    public static final String MODID = "sbmgrowmeal";
    public static final String NAME = "[SBM] Growmeal";
    public static final String PREFIX = MODID + ":";

    public static final String ITEM_NAME = "growmeal";

    @ObjectHolder(Growmeal.PREFIX + ITEM_NAME)
    public static final Item GROWMEAL = null;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ItemGrowmeal(new Item.Properties().tab(CreativeModeTab.TAB_MISC))
                .setRegistryName(new ResourceLocation(Growmeal.MODID, ITEM_NAME)));
    }

}
