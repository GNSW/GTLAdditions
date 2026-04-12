package com.gtladd.gtladditions.data.recipes

import org.gtlcore.gtlcore.common.data.GTLRecipeTypes.MATTER_FABRICATOR_RECIPES

import com.gregtechceu.gtceu.api.GTValues

import net.minecraft.data.recipes.FinishedRecipe

import appeng.core.definitions.AEItems
import com.gtladd.gtladditions.GTLAdditions
import com.gtladd.gtladditions.utils.Registries.getItemStack

import java.util.function.Consumer

object AE2 {
    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        MATTER_FABRICATOR_RECIPES.recipeBuilder(GTLAdditions.id("singularity_1"))
            .inputItems("kubejs:scrap".getItemStack(1080))
            .circuitMeta(3)
            .outputItems(AEItems.SINGULARITY.stack())
            .EUt(GTValues.VA[9].toLong()).duration(4).save(provider)
        MATTER_FABRICATOR_RECIPES.recipeBuilder(GTLAdditions.id("singularity_2"))
            .inputItems("kubejs:scrap_box".getItemStack(120))
            .circuitMeta(3)
            .outputItems(AEItems.SINGULARITY.stack(9))
            .EUt(GTValues.VA[10].toLong()).duration(4).save(provider)
    }
}
