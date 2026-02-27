package com.gtladd.gtladditions.mixin.gtceu.integration;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.integration.GTRecipeWidget;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import com.gtladd.gtladditions.common.recipe.GTLAddRecipesTypes;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(GTRecipeWidget.class)
public class GTRecipeWidgetMixin {

    @Shadow(remap = false)
    @Final
    private GTRecipe recipe;
    @Shadow(remap = false)
    @Final
    private int xOffset;

    @ModifyArg(method = "setRecipeWidget",
               at = @At(value = "INVOKE",
                        target = "Lcom/gregtechceu/gtceu/integration/GTRecipeWidget;addWidget(Lcom/lowdragmc/lowdraglib/gui/widget/Widget;)Lcom/lowdragmc/lowdraglib/gui/widget/WidgetGroup;",
                        ordinal = 3),
               remap = false)
    private Widget setRecipeWidget(Widget par1, @Local(name = "yOffset") int yOffset, @Local(name = "dataInfo") Function<CompoundTag, String> dataInfo) {
        if (recipe.recipeType != GTLAddRecipesTypes.FRACTAL_RECONSTRUCTION) return par1;
        return (new LabelWidget(3 - this.xOffset, yOffset, dataInfo.apply(recipe.data)))
                .setHoverTooltips(Component.translatable("gtceu.recipe.accelerant.hover"));
    }
}
