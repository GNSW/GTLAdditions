package com.gtladd.gtladditions.mixin.gtceu.api.machine;

import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;

import com.gtladd.gtladditions.api.machine.IEnergyMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorkableElectricMultiblockMachine.class)
public abstract class WorkableElectricMultiblockMachineMixin implements IEnergyMachine {

    @Shadow(remap = false)
    protected EnergyContainerList energyContainer;

    @Shadow(remap = false)
    public abstract EnergyContainerList getEnergyContainer();

    @Override
    public EnergyContainerList getEnergyContainerList() {
        if (this.energyContainer == null) this.energyContainer = this.getEnergyContainer();
        return this.energyContainer;
    }
}
