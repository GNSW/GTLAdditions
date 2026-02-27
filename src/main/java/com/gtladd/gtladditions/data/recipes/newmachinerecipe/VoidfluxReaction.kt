package com.gtladd.gtladditions.data.recipes.newmachinerecipe

import org.gtlcore.gtlcore.common.data.GTLMaterials
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA
import org.gtlcore.gtlcore.utils.Registries.getItemStack

import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.GTValues.VA
import com.gregtechceu.gtceu.common.data.GTItems
import com.gregtechceu.gtceu.common.data.GTMaterials.*
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder

import net.minecraft.data.recipes.FinishedRecipe

import com.gtladd.gtladditions.GTLAdditions.id
import com.gtladd.gtladditions.common.recipe.GTLAddRecipesTypes.VOIDFLUX_REACTION
import com.gtladd.gtladditions.common.register.GTLAddItems
import dev.latvian.mods.kubejs.KubeJS

import java.util.*
import java.util.function.Consumer
import java.util.stream.IntStream
import kotlin.math.pow

object VoidfluxReaction {
    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        for (dimension in arrayOf("overworld", "nether", "end")) {
            for (tier in GTValues.UEV..<GTValues.OpV) {
                val voltageName = GTValues.VN[tier].lowercase(Locale.getDefault())
                val builder = VOIDFLUX_REACTION.recipeBuilder(
                    id(String.format("%s_air_collector_%d", dimension, tier - 8))
                )
                    .notConsumable(getItemStack("kubejs:" + dimension + "_data", 64))
                    .notConsumable(getItemStack("gtceu:" + voltageName + "_fluid_regulator"))
                val durations = intArrayOf(20, 200)
                val finalTier = tier - 3
                IntStream.range(0, 2).forEach { mode ->
                    val modeBuilder = (
                        if (mode == 0) {
                            builder.copy(
                                id(String.format("%s_%s_air_collector_%d", voltageName, dimension, mode))
                            ).circuitMeta(1)
                        } else {
                            builder.copy(
                                id(String.format("%s_%s_air_collector_%d", voltageName, dimension, mode))
                            ).notConsumable(MultiBlockMachineA.COOLING_TOWER.asStack())
                        }
                        )
                    setAir(modeBuilder, dimension, mode, 4.0.pow((finalTier - 5).toDouble()).toInt())
                    modeBuilder.duration(durations[mode]).EUt(VA[finalTier].toLong()).save(provider)
                }
            }
        }
        VOIDFLUX_REACTION.recipeBuilder(id("barnarda_air_1"))
            .notConsumable(GTLAddItems.BARNARDA_DATA.asStack(64))
            .notConsumable(GTItems.FLUID_REGULATOR_UEV.asStack(8))
            .outputFluids(GTLMaterials.BarnardaAir.getFluid(1000000))
            .EUt(VA[11].toLong()).duration(500)
            .dimension(KubeJS.id("barnarda"))
            .save(provider)
        VOIDFLUX_REACTION.recipeBuilder(id("barnarda_air_2"))
            .notConsumable(GTLAddItems.BARNARDA_DATA.asStack(64))
            .notConsumable(GTItems.FLUID_REGULATOR_OpV.asStack(8))
            .outputFluids(GTLMaterials.BarnardaAir.getFluid(100000000))
            .EUt(VA[14].toLong()).duration(200)
            .dimension(KubeJS.id("barnarda"))
            .save(provider)
    }

    private fun setAir(builder: GTRecipeBuilder, s: String, i: Int, j: Int) {
        when (s) {
            "overworld" -> {
                if (i == 0) {
                    builder.outputFluids(Air.getFluid(10000L * j))
                } else {
                    builder.outputFluids(LiquidAir.getFluid(10000L * j))
                }
            }
            "nether" -> {
                if (i == 0) {
                    builder.outputFluids(NetherAir.getFluid(10000L * j))
                } else {
                    builder.outputFluids(LiquidNetherAir.getFluid(10000L * j))
                }
            }
            "end" -> {
                if (i == 0) {
                    builder.outputFluids(EnderAir.getFluid(10000L * j))
                } else {
                    builder.outputFluids(LiquidEnderAir.getFluid(10000L * j))
                }
            }
        }
    }
}
