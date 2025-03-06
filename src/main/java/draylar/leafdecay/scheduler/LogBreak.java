package draylar.leafdecay.scheduler;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class LogBreak {
    public static void realize(World world, BlockPos pos, BlockState state, Set<Block> logBlocks) {
        if (!world.isClient && (state.getBlock() instanceof PillarBlock) && logBlocks.stream().anyMatch((block) -> state.getBlock().getClass().isInstance(block))) {
            BlockPos upPosition = pos.up();
            BlockState upState = world.getBlockState(upPosition);

            // trigger chain break on the leaf block above a log
            if (upState.getBlock() instanceof LeavesBlock) {
                FutureBlockBreak futureLeafBreak = new FutureBlockBreak((ServerWorld) world, upPosition, 4);
                LeafBreakHandler.addFutureBreak(futureLeafBreak);
            }
        }
    }
}
