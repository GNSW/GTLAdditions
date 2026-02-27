package com.gtladd.gtladditions.mixin.gtlcore.machine;

import org.gtlcore.gtlcore.common.machine.trait.MultipleRecipesLogic;
import org.gtlcore.gtlcore.mixin.gtm.api.recipe.RecipeLogicAccessor;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.nbt.CompoundTag;

import com.gtladd.gtladditions.api.machine.IEnergyMachine;
import com.gtladd.gtladditions.utils.GTRecipeUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MultipleRecipesLogic.class)
public abstract class MultipleRecipesLogicMixin extends RecipeLogic {

    @Unique
    private long gtladditions$eut;

    public MultipleRecipesLogicMixin(IRecipeLogicMachine machine) {
        super(machine);
    }

    @Shadow(remap = false)
    public abstract WorkableElectricMultiblockMachine getMachine();

    @Override
    public void setupRecipe(GTRecipe recipe) {
        if (this.handleRecipeIO(recipe, IO.IN)) {
            if (this.lastRecipe != null && !recipe.equals(this.lastRecipe)) {
                this.chanceCaches.clear();
            }
            this.gtladditions$eut = GTRecipeUtils.INSTANCE.getGetEU(recipe);
            this.lastRecipe = recipe;
            this.setStatus(Status.WORKING);
            this.progress = 0;
            this.duration = recipe.duration;
            ((RecipeLogicAccessor) this).setIsActive(true);
        }
    }

    @Override
    public void handleRecipeWorking() {
        assert this.lastRecipe != null;
        if (this.gtladditions$eut <= ((IEnergyMachine) getMachine()).getEnergyContainerList().getEnergyStored()) {
            this.setStatus(Status.WORKING);
            ((IEnergyMachine) getMachine()).getEnergyContainerList().changeEnergy(-gtladditions$eut);
            if (!this.machine.onWorking()) {
                this.interruptRecipe();
                return;
            }
            ++this.progress;
            ++this.totalContinuousRunningTime;
        } else this.setWaiting(null);
        if (this.getStatus() == Status.WAITING) this.doDamping();
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        if (tag.contains("eut")) gtladditions$eut = tag.getLong("eut");
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        tag.putLong("eut", gtladditions$eut);
    }
}
