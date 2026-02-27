package com.gtladd.gtladditions.mixin.mc;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import com.gtladd.gtladditions.utils.SingleStream;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.stream.Stream;

@Mixin(Ingredient.class)
public class IngredientMixin {

    @ModifyArg(method = "of(Lnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/crafting/Ingredient;",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/world/item/crafting/Ingredient;fromValues(Ljava/util/stream/Stream;)Lnet/minecraft/world/item/crafting/Ingredient;"),
               remap = false)
    private static Stream of(Stream stream, @Local(name = "tag") TagKey<Item> tag) {
        return SingleStream.Companion.createSingle(new Ingredient.TagValue[] { new Ingredient.TagValue(tag) });
    }
}
