package bl4ckscor3.mod.sbmgrowmeal.datagen;

import bl4ckscor3.mod.sbmgrowmeal.Growmeal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Growmeal.MODID)
public class DataGenRegistrar {
	private DataGenRegistrar() {}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent.Client event) {
		event.createProvider(RecipeGenerator.Runner::new);
	}
}
