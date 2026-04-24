package bl4ckscor3.mod.sbmgrowmeal;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class Growmeal {
	public static final String MODID = "sbmgrowmeal";
	public static final String ITEM_NAME = "growmeal";
	public static final RegistryObject<GrowmealItem> GROWMEAL = RegistryObject.item(ITEM_NAME, GrowmealItem::new, Item.Properties::new);
	private static Platform platform;

	public synchronized static void initialize(Platform platform) {
		if (Growmeal.platform != null) {
			throw new IllegalArgumentException(MODID + " platform has already been initialized");
		}

		Growmeal.platform = platform;
		platform.register(Registries.ITEM, GROWMEAL);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MODID, path);
	}

	public static Platform platform() {
		return platform;
	}
}
