package com.builtbroken.sbmgrowmeal.datagen;

import java.util.concurrent.CompletableFuture;

import com.builtbroken.sbmgrowmeal.Growmeal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	protected final void buildRecipes(RecipeOutput recipeOutput) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Growmeal.GROWMEAL)
		.pattern("b b")
		.pattern(" b ")
		.pattern("b b")
		.define('b', Items.BONE_MEAL)
		.unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
		.save(recipeOutput);
		//@formatter:on
	}
}
