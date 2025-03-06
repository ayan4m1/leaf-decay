package draylar.leafdecay.scheduler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;

public class LeafBreakHandler {

    private static final ArrayList<FutureBlockBreak> breakList = new ArrayList<>();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(tick -> {
            var nearbyFutureBreakOrigins = new ArrayList<FutureBlockBreak>();
            var iterator = breakList.iterator();

            while (iterator.hasNext()) {
                FutureBlockBreak leafBreak = iterator.next();
                // should be broken
                if (leafBreak.getElapsedTime() >= leafBreak.getMaxTime()) {
                    leafBreak.realize();
                    iterator.remove();
                    nearbyFutureBreakOrigins.add(leafBreak);
                }

                // increment time
                else {
                    leafBreak.setElapsedTime(leafBreak.getElapsedTime() + 1);
                }
            }

            // add all new neighbor breaks to the breakList after we iterate over it to avoid CME
            nearbyFutureBreakOrigins.forEach(leafBreak -> addNearbyFutureBreaks(leafBreak.getWorld(), leafBreak.getPos()));
        });
    }

    public static void addNearbyFutureBreaks(ServerWorld world, BlockPos pos) {
        // check all nearby leaf blocks
        // ensure new position is a leaf and that the break list doesn't already contain it
        Arrays.stream(Direction.values()).map(direction -> new BlockPos(pos.offset(direction, 1))).forEach(offset -> {
            BlockState newState = world.getBlockState(offset);
            if (newState.getBlock() instanceof LeavesBlock && !breakListContains(offset)) {
                // if the leaf would naturally decay, add it to the break list
                if (!newState.get(LeavesBlock.PERSISTENT) && newState.get(LeavesBlock.DISTANCE) == 7) {
                    LeafBreakHandler.addFutureBreak(new FutureBlockBreak(world, offset, 5));
                }
            }
        });
    }

    private static boolean breakListContains(BlockPos offset) {
        return breakList.stream().anyMatch(futureBreak ->
                futureBreak.getPos().equals(offset)
        );
    }


    public static void addFutureBreak(FutureBlockBreak futureBreak) {
        breakList.add(futureBreak);
    }
}
