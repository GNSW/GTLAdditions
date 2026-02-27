package com.gtladd.gtladditions.data.recipes

import org.gtlcore.gtlcore.common.data.GTLMachines
import org.gtlcore.gtlcore.common.data.GTLMaterials.*

import com.gregtechceu.gtceu.api.GTValues.*
import com.gregtechceu.gtceu.api.data.tag.TagPrefix.*
import com.gregtechceu.gtceu.common.data.GTBlocks.INDUSTRIAL_TNT
import com.gregtechceu.gtceu.common.data.GTItems.GRAVI_STAR
import com.gregtechceu.gtceu.common.data.GTItems.QUANTUM_STAR
import com.gregtechceu.gtceu.common.data.GTMachines.QUANTUM_CHEST
import com.gregtechceu.gtceu.common.data.GTMachines.SUPER_CHEST
import com.gregtechceu.gtceu.common.data.GTMaterials.*
import com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES

import net.minecraft.data.recipes.FinishedRecipe

import com.gtladd.gtladditions.GTLAdditions.id
import com.gtladd.gtladditions.common.machine.GTLAddMachines
import com.gtladd.gtladditions.utils.Registries.getItemStack

import java.util.*
import java.util.function.Consumer

object Assembler {

    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        addHugeOutput(provider)
        ASSEMBLER_RECIPES.recipeBuilder(id("naquadria_charge_more"))
            .inputItems(QUANTUM_STAR)
            .inputItems(INDUSTRIAL_TNT)
            .inputItems(dust, Naquadria)
            .inputItems(dustTiny, Hexanitrohexaaxaisowurtzitane, 4)
            .inputItems(plateDouble, Thorium)
            .inputFluids(Antimatter.getFluid(1))
            .outputItems("kubejs:naquadria_charge".getItemStack(16))
            .EUt(VA[OpV].toLong()).duration(200).save(provider)
        ASSEMBLER_RECIPES.recipeBuilder(id("leptonic_charge"))
            .inputItems(GRAVI_STAR)
            .inputItems(INDUSTRIAL_TNT)
            .inputItems(dust, DegenerateRhenium)
            .inputItems(dustSmall, Hexanitrohexaaxaisowurtzitane, 2)
            .inputItems(plateDouble, Enderium)
            .inputFluids(Antimatter.getFluid(10))
            .outputItems("kubejs:leptonic_charge".getItemStack(16))
            .EUt(VA[MAX].toLong()).duration(200).save(provider)
        ASSEMBLER_RECIPES.recipeBuilder(id("quantum_chromodynamic_charge"))
            .notConsumable("gtceu:eternity_nanoswarm".getItemStack())
            .inputItems("kubejs:unstable_star".getItemStack())
            .inputItems("kubejs:leptonic_charge".getItemStack())
            .inputItems("kubejs:quantumchromodynamic_protective_plating".getItemStack())
            .inputFluids(Antimatter.getFluid(100))
            .outputItems("kubejs:quantum_chromodynamic_charge".getItemStack(16))
            .duration(200).EUt(4L * VA[MAX]).save(provider)
    }

    private fun addHugeOutput(provider: Consumer<FinishedRecipe>) {
        for (tier in 1..13) {
            val s = VN[tier].lowercase(Locale.getDefault())
            ASSEMBLER_RECIPES.recipeBuilder(id("huge_output_dual_hatch_$s"))
                .inputItems(GTLMachines.HUGE_FLUID_EXPORT_HATCH[tier].asStack())
                .inputItems(if (tier > 4) QUANTUM_CHEST[tier] else SUPER_CHEST[tier])
                .inputFluids(SolderingAlloy.getFluid(144L))
                .outputItems(GTLAddMachines.HUGE_OUTPUT_DUAL_HATCH[tier].asStack())
                .duration(200).EUt(VA[tier].toLong()).save(provider)
        }
    }
}
