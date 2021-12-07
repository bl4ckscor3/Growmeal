package com.builtbroken.sbmgrowmeal;

import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.minecraft.item.Item.Properties;

/**
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2019.
 */
public class ItemGrowmeal extends Item
{
    public static int tries = 1000;

    public ItemGrowmeal(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx)
    {
        final World world = ctx.getLevel();
        final BlockPos pos = ctx.getClickedPos();
        final PlayerEntity player = ctx.getPlayer();
        final Hand hand = ctx.getHand();
        final ItemStack stack = ctx.getItemInHand();

        //world.getBlockState(pos) is never saved as the state will not update otherwhise after applying the meal
        if (isValidToApply(world, pos))
        {
            if (!world.isClientSide)
            {
                if (cycleGrowth(world, pos)) //make sure that the growable has grown before removing an item from the player
                {
                    world.levelEvent(2005, pos, 0); //particles

                    if (!player.isCreative())
                    {
                        stack.shrink(1);
                    }

                    return ActionResultType.SUCCESS;
                }
            }
            player.swing(hand); //swings according to this method's return value
        }
        return ActionResultType.PASS;
    }

    protected boolean cycleGrowth(World world, BlockPos pos)
    {
        //try a thousand times to not have an infinite loop with a growable that can grow infinite times
        for (int i = 0; i < tries; i++)
        {
            if (isValidToApply(world, pos))
            {
                doGrow(world, pos);
                if (!isValidToApply(world, pos))
                {
                    return true;
                }
            }
        }
        return false;
    }

    protected void doGrow(World world, BlockPos pos)
    {
        BlockState blockState = world.getBlockState(pos);
        if (!world.isClientSide && blockState.getBlock() instanceof IGrowable)
        {
            ((IGrowable) blockState.getBlock()).performBonemeal((ServerWorld)world, world.random, pos, blockState);
        }
    }

    protected boolean isValidToApply(World world, BlockPos pos)
    {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof IGrowable)
        {
            return ((IGrowable) blockState.getBlock()).isValidBonemealTarget(world, pos, blockState, world.isClientSide);
        }
        return false;
    }
}
