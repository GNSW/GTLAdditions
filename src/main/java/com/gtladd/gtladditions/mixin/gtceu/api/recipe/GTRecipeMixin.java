package com.gtladd.gtladditions.mixin.gtceu.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.gtladd.gtladditions.api.recipe.IWirelessGTRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GTRecipe.class)
public class GTRecipeMixin implements IWirelessGTRecipe {

    private double wirelessEUt = 0d;

    private IO io = IO.NONE;

    @Override
    public IO getIO() {
        return this.io;
    }

    @Override
    public void setIO(IO io) {
        this.io = io;
    }

    @Override
    public double getWirelessEUt() {
        return this.wirelessEUt;
    }

    @Override
    public void setWirelessEUt(double wirelessEUt) {
        this.wirelessEUt = wirelessEUt;
    }
}
