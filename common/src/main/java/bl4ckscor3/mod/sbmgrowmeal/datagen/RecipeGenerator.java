package bl4ckscor3.mod.sbmgrowmeal.datagen;

import bl4ckscor3.mod.sbmgrowmeal.Growmeal;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeGenerator extends RecipeProvider {
	private final HolderGetter<Item> items;

	public RecipeGenerator(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
		super(recipeOutput, advancementOutput);
		items = recipeOutput.lookup(Registries.ITEM);
	}

	@Override
	public final void buildRecipes() {
		ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, Growmeal.GROWMEAL.get())
			.pattern("b b")
			.pattern(" b ")
			.pattern("b b")
			.define('b', Items.BONE_MEAL)
			.unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
			.save(output);
	}
}
