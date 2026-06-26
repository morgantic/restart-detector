package dev.enjarai.restartdetector.block;

import dev.enjarai.restartdetector.ModConfig;
import dev.enjarai.restartdetector.RestartDetector;
import dev.enjarai.restartdetector.display.SpinnyHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class TpsDetectorBlock extends SpinnyBlock {
    private static final int maxMspt = 50;
    private static final int targetTps = 20;

    public static final IntegerProperty COMPARATOR_POWER = IntegerProperty.create("comparator_power", 0, 15);

    public TpsDetectorBlock(Properties settings) {
        /*? if >=1.21.2 {*/
        super(settings.setId(ResourceKey.create(Registries.BLOCK, RestartDetector.id("tps_detector"))));
        /*?} else {*//*
        super(settings);
        *//*?}*/
        this.registerDefaultState(stateDefinition.any()
                .setValue(POWER, 0)
                .setValue(COMPARATOR_POWER, 0));
    }

    /*? <1.20.5 {*//*
    @Override
    public Block getPolymerBlock(BlockState state) {
        return null;
    }
    *//*?}*/

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        /*? >=1.20.4 {*/
        float mspt = world.getServer().getCurrentSmoothedTickTime();
        /*?} else {*//*
        float mspt = world.getServer().getTickTime();
        *//*?}*/
        float tps = 1000 / Math.max(mspt, maxMspt);

        int comparatorPower = Math.min((int) (mspt / maxMspt * 15), 15);
        int regularPower = Mth.clamp((int) (tps / targetTps * 15), 0, 15);

        world.setBlockAndUpdate(pos, state.setValue(COMPARATOR_POWER, comparatorPower).setValue(POWER, regularPower));

        super.tick(state, world, pos, random);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    /*? >=1.21.9 {*/
    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return state.getValue(COMPARATOR_POWER);
    }
    /*?} else {*//*
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(COMPARATOR_POWER);
    }
    *//*?}*/

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COMPARATOR_POWER);
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return new SpinnyHolder(world, Items.REPEATING_COMMAND_BLOCK.getDefaultInstance()) {
            @Override
            public float getSpeed() {
                var msptFraction = this.world.getBlockState(pos).getValue(COMPARATOR_POWER) / 15f;
                return 60 * (1 - msptFraction);
            }
        };
    }
}
