package draylar.leafdecay.mixin;

import draylar.leafdecay.scheduler.LegacyLogBreakHandler;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class LegacyLogBreakMixin {
    @Inject(
            method = "onBreak",
            at = @At("TAIL")
    )
    private void afterBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo cir) {
        LegacyLogBreakHandler.handleBreak(world, pos, state);
    }
}