package com.gtladd.gtladditions.data.recipes.process

import org.gtlcore.gtlcore.api.machine.multiblock.GTLCleanroomType.LAW_CLEANROOM
import org.gtlcore.gtlcore.common.data.GTLMaterials.*
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes.*
import org.gtlcore.gtlcore.common.recipe.condition.GravityCondition

import com.gregtechceu.gtceu.api.GTValues.*
import com.gregtechceu.gtceu.api.data.chemical.material.Material
import com.gregtechceu.gtceu.api.data.tag.TagPrefix.*
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType
import com.gregtechceu.gtceu.common.data.GTItems
import com.gregtechceu.gtceu.common.data.GTMaterials.*
import com.gregtechceu.gtceu.common.data.GTRecipeTypes.*

import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.item.Item

import com.gtladd.gtladditions.GTLAdditions.id
import com.gtladd.gtladditions.common.recipe.GTLAddRecipesTypes
import com.gtladd.gtladditions.common.register.GTLAddItems
import com.gtladd.gtladditions.common.register.GTLAddMaterial
import com.gtladd.gtladditions.utils.Registries.getItemStack
import com.tterrag.registrate.util.entry.ItemEntry

import java.util.function.Consumer

object SocProcess {
    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        ElectricBlastFurnace.init(provider)
        Cutter.init(provider)
        EngravingArray.init(provider)
        CircuitAssembler.init(provider)
        generatedRecipe(provider)
    }

    private fun generatedRecipe(provider: Consumer<FinishedRecipe>) {
        CHEMICAL_BATH_RECIPES.recipeBuilder(id("bioware_echo_shard_wafer"))
            .inputItems(GTLAddItems.ECHO_SHARD_WAFER.asStack())
            .inputFluids(BiohmediumSterilized.getFluid(250))
            .outputItems(GTLAddItems.BIOWARE_ECHO_SHARD_BOULE.asStack())
            .EUt(VA[UHV].toLong()).duration(200)
            .cleanroom(CleanroomType.STERILE_CLEANROOM)
            .addCondition(GravityCondition()).save(provider)
        LARGE_CHEMICAL_RECIPES.recipeBuilder(id("prepare_extraordinary_soc_wafer"))
            .inputItems(GTLAddItems.HASSIUM_WAFER.asStack())
            .inputItems(dust, FranciumCaesiumCadmiumBromide, 2)
            .inputFluids(SeaborgiumDopedNanotubes.getFluid(144))
            .inputFluids(CarbonNanotubes.getFluid(144))
            .outputItems(GTLAddItems.PREPARE_EXTRAORDINARY_SOC_WAFER.asStack())
            .EUt(VA[UEV].toLong()).duration(200)
            .cleanroom(LAW_CLEANROOM)
            .save(provider)
        SPS_CRAFTING_RECIPES.recipeBuilder(id("dragon_element_starmetal_wafer"))
            .inputItems(GTLAddItems.STARMETAL_WAFER, 4)
            .inputItems("kubejs:kinetic_matter".getItemStack())
            .inputItems("kubejs:unstable_star".getItemStack(2))
            .inputFluids(Mana.getFluid(10000))
            .inputFluids(DragonElement.getFluid(1000))
            .inputFluids(FreeAlphaGas.getFluid(500))
            .outputItems(GTLAddItems.DRAGON_ELEMENT_STARMETAL_WAFER, 4)
            .EUt(VA[UIV].toLong()).duration(200)
            .cleanroom(LAW_CLEANROOM)
            .addCondition(GravityCondition()).save(provider)
        QFT_RECIPES.recipeBuilder(id("prepare_spacetime_soc_wafer"))
            .inputItems(GTLAddItems.PERIODICIUM_WAFER, 4)
            .inputItems("kubejs:charged_lepton_trap_crystal".getItemStack())
            .inputItems("kubejs:nuclear_star".getItemStack(4))
            .inputFluids(CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
            .inputFluids(CosmicElement.getFluid(10000))
            .inputFluids(SpaceTime.getFluid(500))
            .outputItems(GTLAddItems.PREPARE_SPACETIME_SOC_WAFER, 4)
            .EUt(VA[OpV].toLong())
            .duration(200).save(provider)
        DIMENSIONALLY_TRANSCENDENT_MIXER_RECIPES.recipeBuilder(id("prepare_primary_soc_wafer"))
            .notConsumable("kubejs:eternity_catalyst".getItemStack())
            .inputItems(GTLAddItems.INFINITY_WAFER, 4)
            .inputItems("kubejs:quantum_anomaly".getItemStack())
            .inputItems("kubejs:two_way_foil".getItemStack())
            .inputItems("kubejs:void_matter".getItemStack())
            .inputItems(dust, TranscendentMetal, 16)
            .inputFluids(RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 10000))
            .inputFluids(SpaceTime.getFluid(1000))
            .inputFluids(PrimordialMatter.getFluid(1000))
            .outputItems(GTLAddItems.PREPARE_PRIMARY_SOC_WAFER, 4)
            .EUt(4L * VA[MAX]).duration(200)
            .cleanroom(LAW_CLEANROOM).addCondition(GravityCondition())
            .save(provider)
        PRECISION_ASSEMBLER_RECIPES.recipeBuilder(id("spacetime_lens"))
            .inputItems("kubejs:grating_lithography_mask".getItemStack())
            .inputItems("kubejs:topological_manipulator_unit".getItemStack())
            .inputItems("kubejs:ctc_computational_unit".getItemStack())
            .inputFluids(QuantumDots.getFluid(1000))
            .inputFluids(CosmicComputingMixture.getFluid(1000))
            .inputFluids(Krypton.getFluid(10000))
            .outputItems(GTLAddItems.SPACETIME_LENS)
            .EUt(VA[OpV].toLong()).duration(2000)
            .cleanroom(LAW_CLEANROOM).save(provider)
    }

    private object ElectricBlastFurnace {
        fun init(provider: Consumer<FinishedRecipe>) {
            BLAST_RECIPES.recipeBuilder(id("echo_shard_boule"))
                .inputItems(GTItems.SILICON_BOULE, 64)
                .inputItems(dust, GTLAddMaterial.GALLIUM_OXIDE, 16)
                .inputItems("gtceu:echo_shard_dust".getItemStack(16))
                .inputFluids(Krypton.getFluid(16000))
                .outputItems(GTLAddItems.ECHO_SHARD_BOULE)
                .EUt(VA[UV].toLong()).duration(21000)
                .blastFurnaceTemp(14400).save(provider)
            addBlastRecipe(Hassium, GTLAddItems.HASSIUM_BOULE, VA[UHV], 24000, 18000, provider)
            addBlastRecipe(Starmetal, GTLAddItems.STARMETAL_BOULE, VA[UEV], 27000, 21000, provider)
            addBlastRecipe(Periodicium, GTLAddItems.PERIODICIUM_BOULE, VA[UXV], 30000, 36000, provider)
            addBlastRecipe(Infinity, GTLAddItems.INFINITY_BOULE, VA[OpV], 33000, 62000, provider)
            CHEMICAL_RECIPES.recipeBuilder(id("ammonium_gallium_sulfate"))
                .inputItems(dust, Gallium)
                .inputFluids(SulfuricAcid.getFluid(2000))
                .inputFluids(Ammonia.getFluid(1000))
                .outputItems(dust, GTLAddMaterial.AMMONIUM_GALIUM_SULFATE)
                .EUt(VA[EV].toLong()).duration(200).save(provider)
            LARGE_CHEMICAL_RECIPES.recipeBuilder(id("ammonium_gallium_sulfate"))
                .inputItems(dust, Gallium)
                .inputFluids(SulfuricAcid.getFluid(2000))
                .inputFluids(Ammonia.getFluid(1000))
                .outputItems(dust, GTLAddMaterial.AMMONIUM_GALIUM_SULFATE)
                .EUt(VA[EV].toLong()).duration(200).save(provider)
            LARGE_CHEMICAL_RECIPES.recipeBuilder(id("gallium_oxide"))
                .inputItems(dust, GTLAddMaterial.AMMONIUM_GALIUM_SULFATE, 4)
                .inputFluids(Oxygen.getFluid(15000))
                .outputItems(dust, GTLAddMaterial.GALLIUM_OXIDE, 2)
                .outputFluids(Nitrogen.getFluid(2000))
                .outputFluids(SulfurTrioxide.getFluid(8000))
                .outputFluids(Water.getFluid(8000))
                .EUt(VA[IV].toLong()).duration(200)
                .save(provider)
        }

        fun addBlastRecipe(input: Material, output: ItemEntry<Item>, EUt: Int, duration: Int, temperature: Int, provider: Consumer<FinishedRecipe>) {
            BLAST_RECIPES.recipeBuilder(id(output.asItem().toString()))
                .inputItems(GTItems.SILICON_BOULE, 64)
                .inputItems(dust, GTLAddMaterial.GALLIUM_OXIDE, 16)
                .inputItems(dust, input, 16)
                .inputFluids(Krypton.getFluid(16000))
                .outputItems(output).EUt(EUt.toLong())
                .duration(duration).blastFurnaceTemp(temperature).save(provider)
        }
    }

    private object Cutter {
        fun init(provider: Consumer<FinishedRecipe>) {
            addCutterRecipe("echo_shard_wafer", GTLAddItems.ECHO_SHARD_BOULE, 16, GTLAddItems.ECHO_SHARD_WAFER, VA[UV], CleanroomType.STERILE_CLEANROOM, provider)
            addCutterRecipe("outstanding_soc", GTLAddItems.OUTSTANDING_SOC_WAFER, 6, GTLAddItems.OUTSTANDING_SOC, VA[UV], CleanroomType.STERILE_CLEANROOM, provider)
            addCutterRecipe("hassium_wafer", GTLAddItems.HASSIUM_BOULE, 16, GTLAddItems.HASSIUM_WAFER, VA[UHV], LAW_CLEANROOM, provider)
            addCutterRecipe("extraordinary_soc_wafer", GTLAddItems.EXTRAORDINARY_SOC_WAFER, 6, GTLAddItems.EXTRAORDINARY_SOC, VA[UHV], LAW_CLEANROOM, provider)
            addCutterRecipe("starmetal_wafer", GTLAddItems.STARMETAL_BOULE, 16, GTLAddItems.STARMETAL_WAFER, VA[UEV], LAW_CLEANROOM, provider)
            addCutterRecipe("chaos_soc", GTLAddItems.CHAOS_SOC_WAFER, 6, GTLAddItems.CHAOS_SOC, VA[UEV], LAW_CLEANROOM, provider)
            addCutterRecipe("periodicium_wafer", GTLAddItems.PERIODICIUM_BOULE, 256, GTLAddItems.PERIODICIUM_WAFER, VA[UXV], LAW_CLEANROOM, provider)
            addCutterRecipe("spacetime_soc", GTLAddItems.SPACETIME_SOC_WAFER, 6, GTLAddItems.SPACETIME_SOC, VA[UXV], LAW_CLEANROOM, provider)
            addCutterRecipe("infinity_wafer", GTLAddItems.INFINITY_BOULE, 16, GTLAddItems.INFINITY_WAFER, VA[OpV], LAW_CLEANROOM, provider)
            addCutterRecipe("primary_soc", GTLAddItems.PRIMARY_SOC_WAFER, 6, GTLAddItems.PRIMARY_SOC, VA[OpV], LAW_CLEANROOM, provider)
        }

        private fun addCutterRecipe(id: String, input: ItemEntry<Item>, output: Int, outputItem: ItemEntry<Item>, EUt: Int, cleanroomType: CleanroomType, provider: Consumer<FinishedRecipe>) {
            val builder = CUTTER_RECIPES.recipeBuilder(id(id + "_0"))
                .inputItems(input).outputItems(outputItem, output).EUt(EUt.toLong()).cleanroom(cleanroomType)
            if (EUt > VA[UEV]) {
                builder.inputFluids(GradePurifiedWater16.getFluid((if (EUt > VA[UXV]) 1000 else 500).toLong())).duration(450).save(provider)
                return
            }
            val recipe = builder.copy(id(id + "_1"))
            builder.inputFluids(GradePurifiedWater8.getFluid(500)).duration(900).save(provider)
            recipe.inputFluids(GradePurifiedWater16.getFluid(250)).duration(450).save(provider)
        }
    }

    private object EngravingArray {
        fun init(provider: Consumer<FinishedRecipe>) {
            addEngravingRecipe(GTLAddItems.BIOWARE_ECHO_SHARD_BOULE, Photoresist, "kubejs:grating_lithography_mask", GTLAddItems.OUTSTANDING_SOC_WAFER, VA[UHV], 300, CleanroomType.STERILE_CLEANROOM, provider)
            addEngravingRecipe(GTLAddItems.PREPARE_EXTRAORDINARY_SOC_WAFER, Photoresist, "kubejs:grating_lithography_mask", GTLAddItems.EXTRAORDINARY_SOC_WAFER, VA[UEV], 400, LAW_CLEANROOM, provider)
            addEngravingRecipe(GTLAddItems.DRAGON_ELEMENT_STARMETAL_WAFER, EuvPhotoresist, "kubejs:grating_lithography_mask", GTLAddItems.CHAOS_SOC_WAFER, VA[UIV], 500, LAW_CLEANROOM, provider)
            addEngravingRecipe(GTLAddItems.PREPARE_SPACETIME_SOC_WAFER, GammaRaysPhotoresist, "gtladditions:spacetime_lens", GTLAddItems.SPACETIME_SOC_WAFER, VA[UXV], 600, LAW_CLEANROOM, provider)
            addEngravingRecipe(GTLAddItems.PREPARE_PRIMARY_SOC_WAFER, GammaRaysPhotoresist, "gtladditions:spacetime_lens", GTLAddItems.PRIMARY_SOC_WAFER, VA[OpV], 800, LAW_CLEANROOM, provider)
        }

        private fun addEngravingRecipe(input: ItemEntry<Item>, fluid: Material, noinput: String, output: ItemEntry<Item>, EUt: Int, duration: Int, cleanroomType: CleanroomType, provider: Consumer<FinishedRecipe>) {
            DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder(id(output.asItem().toString()))
                .inputItems(input).notConsumable(noinput.getItemStack())
                .inputFluids(fluid.getFluid(100))
                .outputItems(output).EUt(EUt.toLong()).duration(duration)
                .cleanroom(cleanroomType).save(provider)
            GTLAddRecipesTypes.PHOTON_MATRIX_ETCH.recipeBuilder(id(output.asItem().toString()))
                .inputItems(input).notConsumable(noinput.getItemStack())
                .inputFluids(fluid.getFluid(75))
                .outputItems(output).EUt((EUt / 4).toLong()).duration((duration * 0.75).toInt())
                .cleanroom(cleanroomType).save(provider)
        }
    }

    private object CircuitAssembler {
        fun init(provider: Consumer<FinishedRecipe>) {
            addCircuitRecipe("bioware_processor", "kubejs:bioware_printed_circuit_board", GTLAddItems.OUTSTANDING_SOC, Naquadah, Quantanium, "kubejs:bioware_processor", VA[UHV], CleanroomType.STERILE_CLEANROOM, provider)
            addCircuitRecipe("optical_processor", "kubejs:optical_printed_circuit_board", GTLAddItems.EXTRAORDINARY_SOC, Dubnium, Vibranium, "kubejs:optical_processor", VA[UEV], LAW_CLEANROOM, provider)
            addCircuitRecipe("exotic_processor", "kubejs:exotic_printed_circuit_board", GTLAddItems.CHAOS_SOC, Cinobite, HastelloyX78, "kubejs:exotic_processor", VA[UIV], CleanroomType.STERILE_CLEANROOM, provider)
            generateCircuitRecipes(provider)
        }

        private fun addCircuitRecipe(id: String, inputs: String, input: ItemEntry<Item>, material1: Material, material2: Material, output: String, EUt: Int, cleanroomType: CleanroomType, provider: Consumer<FinishedRecipe>) {
            val builder = CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(id(id + "_0"))
                .inputItems(inputs.getItemStack()).inputItems(input).inputItems(wireFine, material1, 8).inputItems(bolt, material2, 8)
                .outputItems(output.getItemStack(4)).EUt(EUt.toLong()).cleanroom(cleanroomType)
            if (EUt > VA[UEV]) {
                builder.inputFluids(SuperMutatedLivingSolder.getFluid(144)).duration(if (EUt <= VA[UEV]) 200 else 150).save(provider)
                return
            }
            val recipe = builder.copy(id(id + "_1"))
            builder.inputFluids(MutatedLivingSolder.getFluid(144)).duration(if (EUt <= VA[UEV]) 200 else 150).save(provider)
            recipe.inputFluids(SuperMutatedLivingSolder.getFluid(72)).duration(if (EUt <= VA[UEV]) 200 else 150).save(provider)
        }

        private fun generateCircuitRecipes(provider: Consumer<FinishedRecipe>) {
            CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(id("cosmic_processor"))
                .inputItems("kubejs:cosmic_printed_circuit_board".getItemStack())
                .inputItems(GTLAddItems.SPACETIME_SOC)
                .inputItems(wireFine, HastelloyX78, 8)
                .inputItems(plate, Crystalmatrix)
                .inputFluids(SuperMutatedLivingSolder.getFluid(288))
                .EUt(VA[UXV].toLong()).cleanroom(LAW_CLEANROOM)
                .outputItems("kubejs:cosmic_processor".getItemStack(4))
                .duration(150).save(provider)
            CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(id("supracausal_processor"))
                .inputItems("kubejs:supracausal_printed_circuit_board".getItemStack())
                .inputItems(GTLAddItems.PRIMARY_SOC)
                .inputItems(wireGtDouble, Hypogen, 4)
                .inputItems(plate, DraconiumAwakened)
                .inputFluids(SuperMutatedLivingSolder.getFluid(360))
                .EUt(VA[OpV].toLong()).cleanroom(LAW_CLEANROOM)
                .outputItems("kubejs:supracausal_processor".getItemStack(4))
                .duration(150).save(provider)
        }
    }
}
