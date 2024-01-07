package com.selfdot.dimensionalkeepinventory;

import com.mojang.brigadier.CommandDispatcher;
import com.selfdot.dimensionalkeepinventory.command.KeepInvCommandTree;
import com.selfdot.dimensionalkeepinventory.util.DisableableMod;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class DimensionalKeepInventoryMod extends DisableableMod {

    public static WorldKeepInventoryTracker WORLD_KEEP_INVENTORY_TRACKER;

    @Override
    public void onInitialize() {
        WORLD_KEEP_INVENTORY_TRACKER = new WorldKeepInventoryTracker(this);

        CommandRegistrationCallback.EVENT.register(this::registerCommands);
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
    }

    private void registerCommands(
        CommandDispatcher<ServerCommandSource> dispatcher,
        CommandRegistryAccess commandRegistryAccess,
        CommandManager.RegistrationEnvironment registrationEnvironment
    ) {
        new KeepInvCommandTree().register(dispatcher);
    }

    private void onServerStarted(MinecraftServer server) {
        WORLD_KEEP_INVENTORY_TRACKER.setServer(server);
        WORLD_KEEP_INVENTORY_TRACKER.load();
    }

    private void onServerStopping(MinecraftServer server) {
        if (!isDisabled()) WORLD_KEEP_INVENTORY_TRACKER.save();
    }

}
