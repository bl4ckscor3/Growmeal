package com.builtbroken.sbmgrowmeal;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Growmeal.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public class Growmeal
{
	public static final String MODID = "sbmgrowmeal";
	public static final String ITEM_NAME = "growmeal";

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final RegistryObject<GrowmealItem> XP_BLOCK_ITEMS = ITEMS.register(ITEM_NAME, () -> new GrowmealItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	public Growmeal()
	{
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
