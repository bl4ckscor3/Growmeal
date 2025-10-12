package com.builtbroken.sbmgrowmeal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2019.
 */
public class GrowmealItem extends Item {
	public static int tries = 1000;

	public GrowmealItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		BlockPos clickedPos = ctx.getClickedPos();
		BlockPos relativePos = clickedPos.relative(ctx.getClickedFace());

		if (cycleGrowth(level, clickedPos, ctx.getPlayer(), BoneMealItem::applyBonemeal)) {
			if (!level.isClientSide()) {
				ctx.getItemInHand().shrink(1);
				ctx.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
				level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, clickedPos, 15);
			}

			return InteractionResult.SUCCESS_SERVER;
		}
		else {
			BlockState clickedState = level.getBlockState(clickedPos);
			boolean isFaceSturdy = clickedState.isFaceSturdy(level, clickedPos, ctx.getClickedFace());

			if (isFaceSturdy && cycleGrowth(level, relativePos, ctx.getClickedFace(), BoneMealItem::growWaterPlant)) {
				if (!level.isClientSide()) {
					ctx.getItemInHand().shrink(1);
					ctx.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
					level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, relativePos, 15);
				}

				return InteractionResult.SUCCESS_SERVER;
			}
			else {
				return InteractionResult.PASS;
			}
		}
	}

	protected <T> boolean cycleGrowth(Level level, BlockPos posToGrow, T arg, PlantGrower<T> grower) {
		boolean hasGrown = false;
		ItemStack stack = new ItemStack(Items.BONE_MEAL, 2);

		//try a thousand times to not have an infinite loop with a growable that can grow infinite times
		for (int i = 0; i < tries; i++) {
			if (grower.grow(stack, level, posToGrow, arg)) {
				stack.grow(1);
				hasGrown = true;
			}
			else if (hasGrown) {
				return true;
			}
			else {
				break;
			}
		}

		return false;
	}

	@FunctionalInterface
	public interface PlantGrower<T> {
		public boolean grow(ItemStack handItem, Level level, BlockPos posToGrow, T arg);
	}
}
