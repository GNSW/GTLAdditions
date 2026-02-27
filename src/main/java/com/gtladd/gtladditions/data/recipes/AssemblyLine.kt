package com.gtladd.gtladditions.data.recipes

import org.gtlcore.gtlcore.common.data.GTLBlocks.IMPROVED_SUPERCONDUCTOR_COIL
import org.gtlcore.gtlcore.common.data.GTLMachines.*
import org.gtlcore.gtlcore.common.data.GTLMaterials.*

import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.data.chemical.material.Material
import com.gregtechceu.gtceu.api.data.tag.TagPrefix.*
import com.gregtechceu.gtceu.common.data.GTMachines.POWER_SUBSTATION
import com.gregtechceu.gtceu.common.data.GTMaterials.*
import com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES

import net.minecraft.data.recipes.FinishedRecipe

import com.gtladd.gtladditions.GTLAdditions
import com.gtladd.gtladditions.common.machine.GTLAddMachines.LASER_INPUT_HATCH_16777216A
import com.gtladd.gtladditions.common.machine.GTLAddMachines.LASER_INPUT_HATCH_67108863A
import com.gtladd.gtladditions.common.machine.GTLAddMachines.LASER_OUTPUT_HATCH_16777216A
import com.gtladd.gtladditions.common.machine.GTLAddMachines.LASER_OUTPUT_HATCH_67108863A
import com.gtladd.gtladditions.common.machine.GTLAddMachines.WIRELESS_LASER_INPUT_HATCH_16777216A
import com.gtladd.gtladditions.common.machine.GTLAddMachines.WIRELESS_LASER_INPUT_HATCH_67108864A
import com.gtladd.gtladditions.common.machine.GTLAddMachines.WIRELESS_LASER_OUTPUT_HATCH_16777216A
import com.gtladd.gtladditions.common.machine.GTLAddMachines.WIRELESS_LASER_OUTPUT_HATCH_67108863A

import java.util.*
import java.util.function.Consumer

object AssemblyLine {
    private val wire = arrayOf<Material>(SamariumIronArsenicOxide, IndiumTinBariumTitaniumCuprate, UraniumRhodiumDinaquadide, EnrichedNaquadahTriniumEuropiumDuranide, RutheniumTriniumAmericiumNeutronate, Enderite, Echoite, Legendarium, DraconiumAwakened, Infinity)
    private val cable = arrayOf<Material>(Graphene, NiobiumTitanium, Trinium, NaquadahAlloy, Mendelevium, Mithril, Adamantine, NaquadriaticTaranium, Starmetal, CosmicNeutronium)

    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        addLaserHatchRecipe(provider)
    }

    private fun addLaserHatchRecipe(provider: Consumer<FinishedRecipe>) {
        for (i in GTValues.IV..14) {
            val tier = GTValues.VN[i].lowercase(Locale.getDefault())
            ASSEMBLY_LINE_RECIPES.recipeBuilder(GTLAdditions.id(tier + "_16777216a_laser_source_hatch"))
                .EUt(GTValues.VA[i].toLong()).duration(200)
                .inputItems(LASER_OUTPUT_HATCH_4194304[i])
                .inputItems(lens, NetherStar, 64)
                .inputItems(IMPROVED_SUPERCONDUCTOR_COIL.asStack(64))
                .inputItems(POWER_SUBSTATION.asStack(64))
                .inputItems(wireGtQuadruple, wire[i - 5], 16)
                .inputItems(cableGtQuadruple, cable[i - 5], 16)
                .circuitMeta(1)
                .inputFluids(SodiumPotassium.getFluid(10000))
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(LASER_OUTPUT_HATCH_16777216A[i])
                .save(provider)
            ASSEMBLY_LINE_RECIPES.recipeBuilder(GTLAdditions.id(tier + "_67108863a_laser_source_hatch"))
                .EUt(GTValues.VA[i].toLong()).duration(200)
                .inputItems(LASER_OUTPUT_HATCH_4194304[i])
                .inputItems(lens, NetherStar, 256)
                .inputItems(IMPROVED_SUPERCONDUCTOR_COIL.asStack(256))
                .inputItems(POWER_SUBSTATION.asStack(256))
                .inputItems(wireGtQuadruple, wire[i - 5], 32)
                .inputItems(cableGtQuadruple, cable[i - 5], 32)
                .circuitMeta(2)
                .inputFluids(SodiumPotassium.getFluid(10000))
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(LASER_OUTPUT_HATCH_67108863A[i])
                .save(provider)
            ASSEMBLY_LINE_RECIPES.recipeBuilder(GTLAdditions.id(tier + "_16777216a_laser_target_hatch"))
                .EUt(GTValues.VA[i].toLong()).duration(200)
                .inputItems(LASER_INPUT_HATCH_4194304[i])
                .inputItems(lens, NetherStar, 64)
                .inputItems(IMPROVED_SUPERCONDUCTOR_COIL.asStack(64))
                .inputItems(POWER_SUBSTATION.asStack(64))
                .inputItems(wireGtQuadruple, wire[i - 5], 16)
                .inputItems(cableGtQuadruple, cable[i - 5], 16)
                .circuitMeta(4)
                .inputFluids(SodiumPotassium.getFluid(10000))
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(LASER_INPUT_HATCH_16777216A[i])
                .save(provider)
            ASSEMBLY_LINE_RECIPES.recipeBuilder(GTLAdditions.id(tier + "_67108864a_laser_target_hatch"))
                .EUt(GTValues.VA[i].toLong()).duration(200)
                .inputItems(LASER_INPUT_HATCH_4194304[i])
                .inputItems(lens, NetherStar, 256)
                .inputItems(IMPROVED_SUPERCONDUCTOR_COIL.asStack(256))
                .inputItems(POWER_SUBSTATION.asStack(256))
                .inputItems(wireGtQuadruple, wire[i - 5], 32)
                .inputItems(cableGtQuadruple, cable[i - 5], 32)
                .circuitMeta(5)
                .inputFluids(SodiumPotassium.getFluid(10000))
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(LASER_INPUT_HATCH_67108863A[i])
                .save(provider)
            ASSEMBLY_LINE_RECIPES.recipeBuilder(GTLAdditions.id(tier + "_16777216a_wireless_laser_source_hatch"))
                .EUt(GTValues.VA[i].toLong()).duration(200)
                .inputItems(WIRELESS_ENERGY_OUTPUT_HATCH_4194304A[i])
                .inputItems(lens, NetherStar, 64)
                .inputItems(IMPROVED_SUPERCONDUCTOR_COIL.asStack(64))
                .inputItems(POWER_SUBSTATION.asStack(64))
                .inputItems(wireGtQuadruple, wire[i - 5], 16)
                .inputItems(cableGtQuadruple, cable[i - 5], 16)
                .circuitMeta(1)
                .inputFluids(SodiumPotassium.getFluid(10000))
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(WIRELESS_LASER_OUTPUT_HATCH_16777216A[i])
                .save(provider)
            ASSEMBLY_LINE_RECIPES.recipeBuilder(GTLAdditions.id(tier + "_67108863a_wireless_laser_source_hatch"))
                .EUt(GTValues.VA[i].toLong()).duration(200)
                .inputItems(WIRELESS_ENERGY_OUTPUT_HATCH_4194304A[i])
                .inputItems(lens, NetherStar, 256)
                .inputItems(IMPROVED_SUPERCONDUCTOR_COIL.asStack(256))
                .inputItems(POWER_SUBSTATION.asStack(256))
                .inputItems(wireGtQuadruple, wire[i - 5], 32)
                .inputItems(cableGtQuadruple, cable[i - 5], 32)
                .circuitMeta(2)
                .inputFluids(SodiumPotassium.getFluid(10000))
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(WIRELESS_LASER_OUTPUT_HATCH_67108863A[i])
                .save(provider)
            ASSEMBLY_LINE_RECIPES.recipeBuilder(GTLAdditions.id(tier + "_16777216a_wireless_laser_target_hatch"))
                .EUt(GTValues.VA[i].toLong()).duration(200)
                .inputItems(WIRELESS_ENERGY_INPUT_HATCH_4194304A[i])
                .inputItems(lens, NetherStar, 64)
                .inputItems(IMPROVED_SUPERCONDUCTOR_COIL.asStack(64))
                .inputItems(POWER_SUBSTATION.asStack(64))
                .inputItems(wireGtQuadruple, wire[i - 5], 16)
                .inputItems(cableGtQuadruple, cable[i - 5], 16)
                .circuitMeta(4)
                .inputFluids(SodiumPotassium.getFluid(10000))
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(WIRELESS_LASER_INPUT_HATCH_16777216A[i])
                .save(provider)
            ASSEMBLY_LINE_RECIPES.recipeBuilder(GTLAdditions.id(tier + "_67108864a_wireless_laser_target_hatch"))
                .EUt(GTValues.VA[i].toLong()).duration(200)
                .inputItems(WIRELESS_ENERGY_INPUT_HATCH_4194304A[i])
                .inputItems(lens, NetherStar, 256)
                .inputItems(IMPROVED_SUPERCONDUCTOR_COIL.asStack(256))
                .inputItems(POWER_SUBSTATION.asStack(256))
                .inputItems(wireGtQuadruple, wire[i - 5], 32)
                .inputItems(cableGtQuadruple, cable[i - 5], 32)
                .circuitMeta(5)
                .inputFluids(SodiumPotassium.getFluid(10000))
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(WIRELESS_LASER_INPUT_HATCH_67108864A[i])
                .save(provider)
        }
    }
}
