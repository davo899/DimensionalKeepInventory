package com.selfdot.dimensionalkeepinventory.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.selfdot.dimensionalkeepinventory.util.CommandUtils;
import com.selfdot.dimensionalkeepinventory.util.DisableableMod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static net.minecraft.command.argument.IdentifierArgumentType.identifier;

public class KeepInvCommandTree {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, DisableableMod mod) {
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>
            literal("keepinv")
            .requires(source -> !mod.isDisabled())
            .requires(source -> CommandUtils.hasPermission(source, "selfdot.op.keepinventory"))
            .then(RequiredArgumentBuilder.<ServerCommandSource, Identifier>
                argument("worldID", identifier())
                .suggests(((context, builder) -> {
                    context.getSource().getServer().getWorldRegistryKeys().forEach(
                        world -> builder.suggest(world.getValue().toString())
                    );
                    return builder.buildFuture();
                }))
                .then(RequiredArgumentBuilder.<ServerCommandSource, Boolean>
                    argument("value", bool())
                    .executes(new SetKeepInventoryCommand())
                )
            )
        );
    }

}
