package com.gtladd.gtladditions.mixin.mc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import com.gtladd.gtladditions.utils.SingleStream;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.stream.Stream;

@Mixin(StrictNBTIngredient.class)
public class StrictNBTIngredientMixin {

    @ModifyArg(method = "<init>",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraftforge/common/crafting/AbstractIngredient;<init>(Ljava/util/stream/Stream;)V"),
               remap = false)
    private static Stream StrictNBTIngredient(Stream par1, @Local(name = "stack") ItemStack stack) {
        return SingleStream.Companion.createSingle(new Ingredient.ItemValue[] { new Ingredient.ItemValue(stack) });
    }
}
