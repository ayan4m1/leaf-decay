package draylar.leafdecay.mixin;

import draylar.leafdecay.scheduler.FutureBlockBreak;
import draylar.leafdecay.scheduler.LeafBreakHandler;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mixin(Block.class)
public class LogBreakMixin {
    @Unique
    private final Set<Block> logBlocks = new HashSet<>(Arrays.asList(Blocks.ACACIA_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.MANGROVE_LOG, Blocks.CHERRY_LOG, Blocks.DARK_OAK_LOG, Blocks.SPRUCE_LOG));

    @Inject(
            method = "onBreak",
            at = @At("TAIL")
    )
    private void afterBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo info) {
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