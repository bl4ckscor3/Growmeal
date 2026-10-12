package bl4ckscor3.mod.sbmgrowmeal.datagen;

import bl4ckscor3.mod.sbmgrowmeal.Growmeal;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

@EventBusSubscriber(modid = Growmeal.MODID)
public class DataGenRegistrar {
	private DataGenRegistrar() {}

	@SubscribeEvent
	public static void onGatherDataRegistryEntries(GatherDataRegistryEntriesEvent event) {
		event.add(RecipeProvider.asBootstrap(RecipeGenerator::new));
	}
}
