package draylar.leafdecay.scheduler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PreNineteenLogBreakHandler {
    @Unique
    private static final Set<Block> LOG_BLOCKS = new HashSet<>(Arrays.asList(
            Blocks.ACACIA_LOG,
            Blocks.BIRCH_LOG,
            Blocks.DARK_OAK_LOG,
            Blocks.JUNGLE_LOG,
            Blocks.OAK_LOG,
            Blocks.SPRUCE_LOG
    ));

    public static void handleBreak(World world, BlockPos pos, BlockState state) {
        LogBreak.realize(world, pos, state, LOG_BLOCKS);
    }
}
