package draylar.leafdecay.scheduler;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LogBreakHandler {
    @Unique
    private static final Set<Block> LOG_BLOCKS = new HashSet<>(Arrays.asList(Blocks.ACACIA_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.MANGROVE_LOG, Blocks.CHERRY_LOG, Blocks.DARK_OAK_LOG, Blocks.SPRUCE_LOG));

    public static void handleBreak(World world, BlockPos pos, BlockState state) {
        if (!world.isClient && (state.getBlock() instanceof PillarBlock) && LOG_BLOCKS.stream().anyMatch((block) -> state.getBlock().getClass().isInstance(block))) {
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
