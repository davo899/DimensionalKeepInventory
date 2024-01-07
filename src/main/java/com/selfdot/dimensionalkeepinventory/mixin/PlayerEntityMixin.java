package com.selfdot.dimensionalkeepinventory.mixin;

import com.selfdot.dimensionalkeepinventory.DimensionalKeepInventoryMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow protected abstract void vanishCursedItems();

    @Shadow @Final private PlayerInventory inventory;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;dropInventory()V",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void injectDropInventory(CallbackInfo ci) {
        if (!DimensionalKeepInventoryMod.WORLD_KEEP_INVENTORY_TRACKER.get(getWorld())) {
            vanishCursedItems();
            inventory.dropAll();
        }
        ci.cancel();
    }

}
