package com.selfdot.dimensionalkeepinventory.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.selfdot.dimensionalkeepinventory.DimensionalKeepInventoryMod;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SetKeepInventoryCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        Identifier worldID = IdentifierArgumentType.getIdentifier(context, "worldID");
        boolean value = BoolArgumentType.getBool(context, "value");
        DimensionalKeepInventoryMod.WORLD_KEEP_INVENTORY_TRACKER.put(worldID, value);
        context.getSource().sendMessage(Text.literal("Set " + worldID + " keep inventory to " + value));
        return SINGLE_SUCCESS;
    }

}
