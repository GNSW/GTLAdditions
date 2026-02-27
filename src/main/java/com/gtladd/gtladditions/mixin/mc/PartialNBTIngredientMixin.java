package com.gtladd.gtladditions.mixin.mc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.PartialNBTIngredient;

import com.gtladd.gtladditions.utils.SingleStream;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Set;
import java.util.stream.Stream;

@Mixin(PartialNBTIngredient.class)
public class PartialNBTIngredientMixin {

    @ModifyArg(method = "<init>",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraftforge/common/crafting/AbstractIngredient;<init>(Ljava/util/stream/Stream;)V"),
               remap = false)
    private static Stream PartialNBTIngredient(Stream par1, @Local(name = "items") Set<Item> items, @Local(name = "nbt") CompoundTag nbt) {
        if (items.size() == 1) {
            ItemStack stack = new ItemStack(items.stream().findFirst().get());
            stack.setTag(nbt.copy());
            return SingleStream.Companion.createSingle(new Ingredient.ItemValue[] { new Ingredient.ItemValue(stack) });
        } else return par1;
    }
}
