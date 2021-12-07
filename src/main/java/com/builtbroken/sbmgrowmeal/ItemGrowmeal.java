package com.builtbroken.sbmgrowmeal;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.item.Item.Properties;

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
    public InteractionResult useOn(UseOnContext ctx)
    {
        final Level world = ctx.getLevel();
        final BlockPos pos = ctx.getClickedPos();
        final Player player = ctx.getPlayer();
        final InteractionHand hand = ctx.getHand();
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

                    return InteractionResult.SUCCESS;
                }
            }
            player.swing(hand); //swings according to this method's return value
        }
        return InteractionResult.PASS;
    }

    protected boolean cycleGrowth(Level world, BlockPos pos)
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

    protected void doGrow(Level world, BlockPos pos)
    {
        BlockState blockState = world.getBlockState(pos);
        if (!world.isClientSide && blockState.getBlock() instanceof BonemealableBlock)
        {
            ((BonemealableBlock) blockState.getBlock()).performBonemeal((ServerLevel)world, world.random, pos, blockState);
        }
    }

    protected boolean isValidToApply(Level world, BlockPos pos)
    {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof BonemealableBlock)
        {
            return ((BonemealableBlock) blockState.getBlock()).isValidBonemealTarget(world, pos, blockState, world.isClientSide);
        }
        return false;
    }
}
