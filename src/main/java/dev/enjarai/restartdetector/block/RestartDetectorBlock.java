package dev.enjarai.restartdetector.block;

import dev.enjarai.restartdetector.ModConfig;
import dev.enjarai.restartdetector.RestartDetector;
import dev.enjarai.restartdetector.display.SpinnyHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RestartDetectorBlock extends SpinnyBlock {
    public RestartDetectorBlock(Properties settings) {
        /*? if >=1.21.2 {*/
        super(settings.setId(ResourceKey.create(Registries.BLOCK, RestartDetector.id("restart_detector"))));
        /*?} else {*//*
        super(settings);
        *//*?}*/
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (RestartDetector.isServerStopping()) {
            int totalTicks = ModConfig.INSTANCE.stopCountdownTicks;
            int power = (totalTicks - RestartDetector.getTicksToStop()) * 14 / totalTicks + 1;
            world.setBlockAndUpdate(pos, state.setValue(POWER, power));
        } else {
            world.setBlockAndUpdate(pos, state.setValue(POWER, Math.max(0, state.getValue(POWER) - 1)));
        }

        super.tick(state, world, pos, random);
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return new SpinnyHolder(world, Items.COMMAND_BLOCK.getDefaultInstance()) {
            @Override
            public float getSpeed() {
                float speed = 6;
                if (RestartDetector.isServerStopping()) {
                    var shutdownProgress = this.world.getBlockState(pos).getValue(POWER) / 15f;
                    speed += 60 * shutdownProgress;
                }
                return speed;
            }
        };
    }
}
