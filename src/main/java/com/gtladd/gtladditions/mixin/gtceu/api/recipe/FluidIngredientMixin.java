package com.gtladd.gtladditions.mixin.gtceu.api.recipe;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import com.gtladd.gtladditions.utils.SingleStream;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Arrays;
import java.util.stream.Stream;

@Mixin(FluidIngredient.class)
@SuppressWarnings("all")
public class FluidIngredientMixin {

    @Shadow(remap = false)
    public FluidIngredient.Value[] values;
    @Shadow(remap = false)
    private long amount;
    @Shadow(remap = false)
    private CompoundTag nbt;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public FluidIngredient copy() {
        if (this.values.length == 1) return new FluidIngredient(SingleStream.Companion.createSingle(this.values[0]), this.amount, this.nbt == null ? null : this.nbt.copy());
        return new FluidIngredient(Arrays.stream(this.values), this.amount, this.nbt == null ? null : this.nbt.copy());
    }

    @ModifyArg(method = "of(Lnet/minecraft/tags/TagKey;J)Lcom/gregtechceu/gtceu/api/recipe/ingredient/FluidIngredient;",
               at = @At(value = "INVOKE",
                        target = "Lcom/gregtechceu/gtceu/api/recipe/ingredient/FluidIngredient;fromValues(Ljava/util/stream/Stream;JLnet/minecraft/nbt/CompoundTag;)Lcom/gregtechceu/gtceu/api/recipe/ingredient/FluidIngredient;"),
               index = 0,
               remap = false)
    private static Stream ofNoTag(Stream stream, @Local(name = "tag") TagKey<Fluid> tag) {
        return SingleStream.Companion.createSingle(new FluidIngredient.TagValue[] { new FluidIngredient.TagValue(tag) });
    }

    @ModifyArg(method = "of(Lnet/minecraft/tags/TagKey;JLnet/minecraft/nbt/CompoundTag;)Lcom/gregtechceu/gtceu/api/recipe/ingredient/FluidIngredient;",
               at = @At(value = "INVOKE",
                        target = "Lcom/gregtechceu/gtceu/api/recipe/ingredient/FluidIngredient;fromValues(Ljava/util/stream/Stream;JLnet/minecraft/nbt/CompoundTag;)Lcom/gregtechceu/gtceu/api/recipe/ingredient/FluidIngredient;"),
               index = 0,
               remap = false)
    private static Stream ofIsTag(Stream stream, @Local(name = "tag") TagKey<Fluid> tag) {
        return SingleStream.Companion.createSingle(new FluidIngredient.TagValue[] { new FluidIngredient.TagValue(tag) });
    }
}
