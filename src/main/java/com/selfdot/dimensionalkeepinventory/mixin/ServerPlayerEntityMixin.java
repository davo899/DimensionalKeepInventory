package com.selfdot.dimensionalkeepinventory.mixin;

import com.mojang.authlib.GameProfile;
import com.selfdot.dimensionalkeepinventory.DimensionalKeepInventoryMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(
        method = "copyFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendAbilitiesUpdate()V",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void injectCopyFrom1(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if (!alive && (
            oldPlayer.isSpectator() || DimensionalKeepInventoryMod.WORLD_KEEP_INVENTORY_TRACKER.get(
                oldPlayer.getWorld()
            )
        )) {
            getInventory().clone(oldPlayer.getInventory());
            experienceLevel = oldPlayer.experienceLevel;
            totalExperience = oldPlayer.totalExperience;
            experienceProgress = oldPlayer.experienceProgress;
            setScore(oldPlayer.getScore());
            ci.cancel();
        }
    }

    @Inject(
        method = "copyFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getGameRules()Lnet/minecraft/world/GameRules;",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void injectCopyFrom2(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ci.cancel();
    }

}
