package com.builtbroken.sbmgrowmeal;

import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

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
