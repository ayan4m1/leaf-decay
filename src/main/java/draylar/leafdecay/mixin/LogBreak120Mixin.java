package draylar.leafdecay.mixin;

import draylar.leafdecay.scheduler.LogBreakHandler;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class LogBreak120Mixin {
    @Inject(
            method = "onBreak",
            at = @At("TAIL")
    )
    private void afterBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo cir) {
        LogBreakHandler.handleBreak(world, pos, state);
    }
}