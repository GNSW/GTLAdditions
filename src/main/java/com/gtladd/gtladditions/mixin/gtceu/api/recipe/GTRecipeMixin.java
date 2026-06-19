package com.gtladd.gtladditions.mixin.gtceu.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.gtladd.gtladditions.api.recipe.IWirelessGTRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GTRecipe.class)
public class GTRecipeMixin implements IWirelessGTRecipe {

    @Unique
    private double gTLAdditions$wirelessEUt = 0d;

    @Unique
    private IO gTLAdditions$io = IO.NONE;

    @Override
    public IO getIO() {
        return this.gTLAdditions$io;
    }

    @Override
    public void setIO(IO io) {
        this.gTLAdditions$io = io;
    }

    @Override
    public double getWirelessEUt() {
        return this.gTLAdditions$wirelessEUt;
    }

    @Override
    public void setWirelessEUt(double wirelessEUt) {
        this.gTLAdditions$wirelessEUt = wirelessEUt;
    }
}
