package com.selfdot.dimensionalkeepinventory.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static net.minecraft.command.argument.IdentifierArgumentType.identifier;

public class KeepInvCommandTree {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>
            literal("keepinv")
            .then(RequiredArgumentBuilder.<ServerCommandSource, Identifier>
                argument("worldID", identifier())
                .then(RequiredArgumentBuilder.<ServerCommandSource, Boolean>
                    argument("value", bool())
                    .executes(new SetKeepInventoryCommand())
                )
            )
        );
    }

}
