package com.builtbroken.sbmgrowmeal;

import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Growmeal.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public class Growmeal {
	public static final String MODID = "sbmgrowmeal";
	public static final String ITEM_NAME = "growmeal";
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final RegistryObject<GrowmealItem> GROWMEAL = ITEMS.register(ITEM_NAME, () -> new GrowmealItem(new Item.Properties()));

	public Growmeal() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SubscribeEvent
	public static void onCreativeModeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES || event.getTabKey() == CreativeModeTabs.INGREDIENTS)
			event.getEntries().putAfter(new ItemStack(Items.BONE_MEAL), new ItemStack(GROWMEAL.get()), TabVisibility.PARENT_AND_SEARCH_TABS);
	}
}
