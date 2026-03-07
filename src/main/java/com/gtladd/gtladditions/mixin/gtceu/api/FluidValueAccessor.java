package com.gtladd.gtladditions.mixin.gtceu.api;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.world.level.material.Fluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidIngredient.FluidValue.class)
public interface FluidValueAccessor {

    @Accessor(remap = false)
    Fluid getFluid();
}
