package com.gtladd.gtladditions.mixin.gtlcore.machine.part;

import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.GravityCleaningConfigurationMaintenancePartMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GravityCleaningConfigurationMaintenancePartMachine.class)
public class GravityCleaningConfigurationMaintenancePartMachineMixin extends TieredPartMachine {

    @Shadow(remap = false)
    private boolean isConfig;

    public GravityCleaningConfigurationMaintenancePartMachineMixin(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Override
    public boolean canShared() {
        return isConfig;
    }
}
