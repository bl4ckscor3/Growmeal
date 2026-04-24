package bl4ckscor3.mod.sbmgrowmeal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Growmeal.MODID)
@EventBusSubscriber
public class NeoEntrypoint implements Platform {
	private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registers = new HashMap<>();
	private final IEventBus modBus;

	public NeoEntrypoint(IEventBus modBus) {
		this.modBus = modBus;
		Growmeal.initialize(this);
	}

	@SubscribeEvent
	public static void onCreativeModeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES || event.getTabKey() == CreativeModeTabs.INGREDIENTS)
			event.insertAfter(new ItemStack(Items.BONE_MEAL), new ItemStack(Growmeal.GROWMEAL.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	@Override
	public <R, T extends R> void register(ResourceKey<? extends Registry<R>> registry, Supplier<T> entry, String path) {
		@SuppressWarnings("unchecked")
		DeferredRegister<R> register = (DeferredRegister<R>) registers.computeIfAbsent(
			registry,
			_ -> {
				DeferredRegister<R> r = DeferredRegister.create(registry, Growmeal.MODID);

				r.register(modBus);
				return r;
			}
		);
		register.register(path, entry);
	}
}
