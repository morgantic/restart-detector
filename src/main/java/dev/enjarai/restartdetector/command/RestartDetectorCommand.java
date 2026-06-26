package dev.enjarai.restartdetector.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.enjarai.restartdetector.ModConfig;
import dev.enjarai.restartdetector.RestartDetector;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;


public class RestartDetectorCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("restartdetector")
                .requires(Permissions.require(RestartDetector.MOD_ID + ".command.restartdetector", 4))
                .then(Commands.literal("countdown")
                        .requires(Permissions.require(RestartDetector.MOD_ID + ".command.restartdetector.countdown", 4))
                        .then(Commands.literal("start")
                                .executes(RestartDetectorCommand::startCountdown)
                        )
                        .then(Commands.literal("cancel")
                                .executes(RestartDetectorCommand::cancelCountdown)
                        )
                )
        );
    }

    public static int startCountdown(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(/*? >=1.20 {*/() -> /*?}*/Component.translatable("commands.stop.stopping",
                ModConfig.INSTANCE.stopCountdownTicks / 20, ModConfig.INSTANCE.stopCountdownTicks), true);
        RestartDetector.startStopCountdown();

        return 1;
    }

    public static int cancelCountdown(CommandContext<CommandSourceStack> context) {
        if (RestartDetector.isServerStopping()) {
            context.getSource().sendSuccess(/*? >=1.20 {*/() -> /*?}*/Component.translatable("commands.stop.cancelled"), true);
            RestartDetector.cancelStopCountdown();

            return 1;
        } else {
            context.getSource().sendSuccess(/*? >=1.20 {*/() -> /*?}*/Component.translatable("commands.stop.not_stopping"), true);

            return 0;
        }
    }
}
