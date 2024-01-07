package com.selfdot.dimensionalkeepinventory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.selfdot.dimensionalkeepinventory.util.DisableableMod;
import com.selfdot.dimensionalkeepinventory.util.JsonFile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class WorldKeepInventoryTracker extends JsonFile {

    private final Map<Identifier, Boolean> worldKeepInventoryMap = new HashMap<>();
    private MinecraftServer server;

    public WorldKeepInventoryTracker(DisableableMod mod) {
        super(mod);
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    public boolean get(World world) {
        return worldKeepInventoryMap.getOrDefault(world.getRegistryKey().getValue(), false);
    }

    public void put(Identifier worldID, boolean value) {
        worldKeepInventoryMap.put(worldID, value);
    }

    @Override
    protected JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        worldKeepInventoryMap.forEach((key, value) -> jsonObject.addProperty(key.toString(), value));
        return jsonObject;
    }

    @Override
    protected String filename() {
        return "config/dimensionalKeepInventory/config.json";
    }

    @Override
    protected void setDefaults() {
        worldKeepInventoryMap.clear();
        server.getWorldRegistryKeys().forEach(key -> worldKeepInventoryMap.put(key.getValue(), false));
    }

    @Override
    protected void loadFromJson(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.entrySet().forEach(
            entry -> worldKeepInventoryMap.put(new Identifier(entry.getKey()), entry.getValue().getAsBoolean())
        );
    }

}
