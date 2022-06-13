package com.builtbroken.sbmgrowmeal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2019.
 */
public class GrowmealItem extends Item
{
	public static int tries = 1000;

	public GrowmealItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx)
	{
		final Level level = ctx.getLevel();
		final BlockPos pos = ctx.getClickedPos();
		final Player player = ctx.getPlayer();
		final InteractionHand hand = ctx.getHand();
		final ItemStack stack = ctx.getItemInHand();

		//world.getBlockState(pos) is never saved as the state will not update otherwhise after applying the meal
		if (isValidToApply(level, pos))
		{
			if (!level.isClientSide)
			{
				if (cycleGrowth(level, pos)) //make sure that the growable has grown before removing an item from the player
				{
					level.levelEvent(2005, pos, 0); //particles

					if (!player.isCreative())
					{
						stack.shrink(1);
					}

					return InteractionResult.SUCCESS;
				}
			}
			player.swing(hand); //swings according to this method's return value
		}
		return InteractionResult.PASS;
	}

	protected boolean cycleGrowth(Level level, BlockPos pos)
	{
		//try a thousand times to not have an infinite loop with a growable that can grow infinite times
		for (int i = 0; i < tries; i++)
		{
			if (isValidToApply(level, pos))
			{
				doGrow(level, pos);
				if (!isValidToApply(level, pos))
				{
					return true;
				}
			}
		}
		return false;
	}

	protected void doGrow(Level level, BlockPos pos)
	{
		BlockState blockState = level.getBlockState(pos);
		if (!level.isClientSide && blockState.getBlock() instanceof BonemealableBlock bonemealableBlock)
		{
			bonemealableBlock.performBonemeal((ServerLevel)level, level.random, pos, blockState);
		}
	}

	protected boolean isValidToApply(Level level, BlockPos pos)
	{
		BlockState blockState = level.getBlockState(pos);
		if (blockState.getBlock() instanceof BonemealableBlock bonemealableBlock)
		{
			return bonemealableBlock.isValidBonemealTarget(level, pos, blockState, level.isClientSide);
		}
		return false;
	}
}
