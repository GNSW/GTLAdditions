package com.gtladd.gtladditions.data.recipes

import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix.nanoswarm
import org.gtlcore.gtlcore.common.data.GTLBlocks.CREATE_CASING
import org.gtlcore.gtlcore.common.data.GTLBlocks.POWER_MODULE_5
import org.gtlcore.gtlcore.common.data.GTLBlocks.SPS_CASING
import org.gtlcore.gtlcore.common.data.GTLItems
import org.gtlcore.gtlcore.common.data.GTLItems.EXTREMELY_ULTIMATE_BATTERY
import org.gtlcore.gtlcore.common.data.GTLMachines
import org.gtlcore.gtlcore.common.data.GTLMaterials.*
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes.SUPRACHRONAL_ASSEMBLY_LINE_RECIPES
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine.ASSEMBLER_MODULE
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine.CREATE_COMPUTATION
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine.RESOURCE_COLLECTION
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine.SPACE_ELEVATOR
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine.SPACE_PROBE_SURFACE_RECEPTION
import org.gtlcore.gtlcore.common.data.machines.GeneratorMachine.DYSON_SPHERE
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA.*

import com.gregtechceu.gtceu.api.GTValues.MAX
import com.gregtechceu.gtceu.api.GTValues.UIV
import com.gregtechceu.gtceu.api.GTValues.UXV
import com.gregtechceu.gtceu.api.GTValues.VA
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper
import com.gregtechceu.gtceu.api.data.tag.TagPrefix.*
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient
import com.gregtechceu.gtceu.common.data.GTItems
import com.gregtechceu.gtceu.common.data.GTItems.COVER_SOLAR_PANEL_EV
import com.gregtechceu.gtceu.common.data.GTItems.FIELD_GENERATOR_UEV
import com.gregtechceu.gtceu.common.data.GTItems.TOOL_DATA_MODULE
import com.gregtechceu.gtceu.common.data.GTMachines
import com.gregtechceu.gtceu.common.data.GTMaterials.Neutronium
import com.gregtechceu.gtceu.common.data.GTMaterials.Promethium
import com.gregtechceu.gtceu.common.data.GTMaterials.Rhenium
import com.gregtechceu.gtceu.common.data.GTMaterials.UUMatter
import com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES
import com.gregtechceu.gtceu.data.recipe.CustomTags
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper

import com.lowdragmc.lowdraglib.side.fluid.FluidStack

import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.level.block.Blocks

import com.gtladd.gtladditions.GTLAdditions.id
import com.gtladd.gtladditions.common.machine.GTLAddMachines
import com.gtladd.gtladditions.common.machine.GTLAddMachines.VIENTIANE_TRANSCEIPTION_NODE
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.CATALYTIC_CASCADE_ARRAY
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.FRACTAL_MANIPULATOR
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.FUXI_BAGUA_HEAVEN_FORGING_FURNACE
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.HYPERDIMENSIONAL_ENERGY_CONCETRATOR
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.MAGNETORHEOLOGICAL_CONVERGENCE_CORE
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.PLANETARY_IONISATION_CONVERGENCE_TOWER
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.RECURSIVE_REVERSE_FORGE
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.REVERSE_TIME_BOOSTING_ENGINE
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine.SKELETON_SHIFT_RIFT_ENGINE
import com.gtladd.gtladditions.common.register.GTLAddItems
import com.gtladd.gtladditions.utils.Registries.getFluid
import com.gtladd.gtladditions.utils.Registries.getItemStack
import com.hepdd.gtmthings.data.WirelessMachines

import java.util.function.Consumer

object NewMachineRecipe2 {
    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        VanillaRecipeHelper.addShapedRecipe(
            provider, true, id("vientiane_transcription_node"),
            VIENTIANE_TRANSCEIPTION_NODE.asStack(),
            "ADA", "CBC", "EFE",
            'A', CustomTags.UHV_CIRCUITS,
            'B', "gtceu:advanced_fluid_detector_cover".getItemStack(),
            'C', ChemicalHelper.get(wireFine, Enderite),
            'D', GTMachines.HULL[14].asStack(),
            'E', GTItems.EMITTER_UV.asStack(),
            'F', GTLMachines.HEAT_SENSOR.asStack()
        )
        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(id("recursive_reverse_forge"))
            .inputItems(
                "kubejs:supracausal_mainframe".getItemStack(64),
                "kubejs:suprachronal_max".getItemStack(64)
            )
            .inputItems(FUXI_BAGUA_HEAVEN_FORGING_FURNACE, 16)
            .inputItems(SKELETON_SHIFT_RIFT_ENGINE, 32)
            .inputItems(CREATE_COMPUTATION, 32)
            .inputItems(DIMENSIONALLY_TRANSCENDENT_MIXER, 64)
            .inputItems(block, Magmatter, 64)
            .inputItems(
                CREATE_CASING.asStack(64),
                "kubejs:chaotic_energy_core".getItemStack(8),
                GTLAddItems.PRIMARY_SOC.asStack(64),
                GTLAddItems.PRIMARY_SOC.asStack(32),
                "kubejs:hyperdimensional_drone".getItemStack(48),
                "kubejs:create_aggregatione_core".getItemStack(56)
            )
            .inputItems(plateDouble, MagnetohydrodynamicallyConstrainedStarMatter, 64)
            .inputItems(plateDouble, MagnetohydrodynamicallyConstrainedStarMatter, 64)
            .inputItems("avaritia:singularity".getItemStack(64))
            .inputFluids(
                Miracle.getFluid(100000),
                RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 16384000),
                Shirabon.getFluid(144000),
                CosmicElement.getFluid(14400000)
            )
            .outputItems(RECURSIVE_REVERSE_FORGE)
            .EUt(8192L * VA[MAX])
            .duration(112000)
            .stationResearch {
                it.researchStack(FUXI_BAGUA_HEAVEN_FORGING_FURNACE.asStack())
                    .dataStack(TOOL_DATA_MODULE.asStack())
                    .EUt(VA[MAX]).CWUt(65536)
            }
            .save(provider)
        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(id("reverse_time_boosting_engine"))
            .inputItems(
                "kubejs:supracausal_mainframe".getItemStack(4),
                "kubejs:suprachronal_max".getItemStack(4)
            )
            .inputItems(SKELETON_SHIFT_RIFT_ENGINE, 64)
            .inputItems(SKELETON_SHIFT_RIFT_ENGINE, 48)
            .inputItems("kubejs:dimension_creation_casing".getItemStack(4))
            .inputItems(GTLMachines.NEUTRON_ACCELERATOR[14], 16)
            .inputItems(
                "kubejs:spacetime_catalyst".getItemStack(64),
                GTLItems.FIELD_GENERATOR_MAX.asStack(32),
                CREATE_CASING.asStack(56)
            )
            .inputItems(wireGtHex, SpaceTime, 32)
            .inputItems(plateDouble, Shirabon, 48)
            .inputItems(foil, Cosmic, 64)
            .inputFluids(
                TemporalFluid.getFluid(48000),
                SpatialFluid.getFluid(48000)
            )
            .outputItems(REVERSE_TIME_BOOSTING_ENGINE)
            .EUt(1024L * VA[MAX])
            .duration(23000)
            .stationResearch {
                it.researchStack(SKELETON_SHIFT_RIFT_ENGINE.asStack())
                    .dataStack(TOOL_DATA_MODULE.asStack())
                    .EUt(VA[MAX]).CWUt(16384)
            }
            .save(provider)
        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(id("magnetorheological_convergence_core"))
            .inputItems(
                "kubejs:supracausal_mainframe".getItemStack(4),
                "kubejs:suprachronal_max".getItemStack(4)
            )
            .inputItems(block, Magmatter, 64)
            .inputItems(dust, Magmatter, 48)
            .inputItems("kubejs:recursively_folded_negative_space".getItemStack(48))
            .inputItems(GTLAddItems.SPACETIME_LENS, 64)
            .inputItems(GTLAddItems.PRIMARY_SOC, 24)
            .inputItems(
                GTLItems.FIELD_GENERATOR_MAX.asStack(32),
                CREATE_CASING.asStack(56)
            )
            .inputItems(wireGtHex, SpaceTime, 32)
            .inputItems(plateDouble, Shirabon, 48)
            .inputItems(foil, Cosmic, 64)
            .inputFluids(
                Eternity.getFluid(48000),
                Chaos.getFluid(48000)
            )
            .outputItems(MAGNETORHEOLOGICAL_CONVERGENCE_CORE)
            .EUt(1024L * VA[MAX])
            .duration(23000)
            .stationResearch {
                it.researchStack(ChemicalHelper.get(block, Magmatter, 64))
                    .dataStack(TOOL_DATA_MODULE.asStack())
                    .EUt(VA[MAX]).CWUt(16384)
            }
            .save(provider)
        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(id("catalytic_cascade_array"))
            .inputItems(
                "kubejs:supracausal_mainframe".getItemStack(4),
                "kubejs:suprachronal_max".getItemStack(4)
            )
            .inputItems(DIMENSIONALLY_TRANSCENDENT_MIXER, 64)
            .inputItems(
                "kubejs:create_aggregatione_core".getItemStack(48),
                "avaritia:singularity".getItemStack(64)
            )
            .inputItems(plateDouble, MagnetohydrodynamicallyConstrainedStarMatter, 48)
            .inputItems(
                "kubejs:nuclear_star".getItemStack(48),
                GTLItems.FIELD_GENERATOR_MAX.asStack(32),
                CREATE_CASING.asStack(56)
            )
            .inputItems(wireGtHex, SpaceTime, 32)
            .inputItems(plateDouble, Shirabon, 48)
            .inputItems(foil, Cosmic, 64)
            .inputFluids(
                DimensionallyTranscendentProsaicCatalyst.getFluid(32000),
                DimensionallyTranscendentResplendentCatalyst.getFluid(32000),
                DimensionallyTranscendentExoticCatalyst.getFluid(32000),
                DimensionallyTranscendentStellarCatalyst.getFluid(32000)
            )
            .outputItems(CATALYTIC_CASCADE_ARRAY)
            .EUt(1024L * VA[MAX])
            .duration(23000)
            .stationResearch {
                it.researchStack("kubejs:nuclear_star".getItemStack())
                    .dataStack(TOOL_DATA_MODULE.asStack())
                    .EUt(VA[MAX]).CWUt(16384)
            }
            .save(provider)
        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(id("hyperdimensional_energy_concentrator"))
            .inputItems(
                "kubejs:supracausal_mainframe".getItemStack(4),
                "kubejs:suprachronal_max".getItemStack(4),
                "kubejs:create_ultimate_battery".getItemStack(48),
                "kubejs:chaotic_energy_core".getItemStack(56)
            )
            .inputItems(CREATE_COMPUTATION, 64)
            .inputItems("kubejs:hyperdimensional_drone".getItemStack(8))
            .inputItems(GTLAddMachines.WIRELESS_LASER_INPUT_HATCH_67108864A[14].asStack(64))
            .inputItems(WirelessMachines.WIRELESS_ENERGY_MONITOR.asStack(64))
            .inputItems(CREATE_CASING.asStack(56))
            .inputItems(wireGtHex, SpaceTime, 32)
            .inputItems(plateDouble, Shirabon, 48)
            .inputItems(foil, Cosmic, 64)
            .inputFluids(TranscendentMetal.getFluid(96000))
            .inputFluids(FluidIngredient.of(FluidStack.create("kubejs:gelid_cryotheum".getFluid, 16384000)))
            .outputItems(HYPERDIMENSIONAL_ENERGY_CONCETRATOR)
            .EUt(1024L * VA[MAX])
            .duration(23000)
            .stationResearch {
                it.researchStack(CREATE_COMPUTATION.asStack())
                    .dataStack(TOOL_DATA_MODULE.asStack())
                    .EUt(VA[MAX]).CWUt(16384)
            }
            .save(provider)
        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(id("fractal_manipulator"))
            .inputItems(
                "kubejs:supracausal_mainframe".getItemStack(4),
                "kubejs:suprachronal_max".getItemStack(4)
            )
            .inputItems(QFT, 64)
            .inputItems(CHEMICAL_DISTORT, 64)
            .inputItems(
                "kubejs:eternity_catalyst".getItemStack(32),
                "avaritia:infinity_catalyst".getItemStack(32),
                "kubejs:chain_command_block_core".getItemStack(56),
                "kubejs:repeating_command_block_core".getItemStack(56)
            )
            .inputItems(CREATE_CASING.asStack(56))
            .inputItems(wireGtHex, SpaceTime, 32)
            .inputItems(plateDouble, Shirabon, 48)
            .inputItems(foil, Cosmic, 64)
            .inputFluids(
                MagnetohydrodynamicallyConstrainedStarMatter.getFluid(48000),
                PrimordialMatter.getFluid(144000)
            )
            .outputItems(FRACTAL_MANIPULATOR)
            .EUt(1024L * VA[MAX])
            .duration(23000)
            .stationResearch {
                it.researchStack(Blocks.COMMAND_BLOCK.asItem().defaultInstance)
                    .dataStack(TOOL_DATA_MODULE.asStack())
                    .EUt(VA[MAX]).CWUt(16384)
            }
            .save(provider)

        ASSEMBLY_LINE_RECIPES.recipeBuilder(id("planetary_ionisation_convergence_tower"))
            .inputItems(
                "minecraft:lightning_rod".getItemStack(64)
            )
            .inputItems(
                GTLMachines.LIGHTNING_ROD[4].asStack(64),
                GTLMachines.LIGHTNING_ROD[5].asStack(64),
                GTLMachines.LIGHTNING_ROD[6].asStack(64)
            )
            .inputItems(CustomTags.UEV_CIRCUITS, 16)
            .inputItems(SPS_CASING, 16)
            .inputItems("kubejs:space_drone_mk3".getItemStack(16))
            .inputItems(nanoswarm, Neutronium, 64)
            .inputItems(
                "gtceu:uhv_ultimate_battery".getItemStack(56),
                "gtceu:uhv_ultimate_battery".getItemStack(56)
            )
            .inputItems("kubejs:nm_chip".getItemStack(48))
            .inputItems("gtceu:highly_advanced_soc".getItemStack(48))
            .inputItems(FIELD_GENERATOR_UEV, 64)
            .inputItems(plateDouble, Quantum, 32)
            .inputItems(plateDouble, Dalisenite, 32)
            .inputItems("kubejs:special_ceramics".getItemStack(64))
            .inputFluids(Promethium.getFluid(8192))
            .inputFluids(Rhenium.getFluid(8192))
            .inputFluids(MutatedLivingSolder.getFluid(16384))
            .inputFluids(HastelloyX78.getFluid(9216))
            .outputItems(PLANETARY_IONISATION_CONVERGENCE_TOWER)
            .EUt(VA[UIV].toLong()).duration(6000)
            .stationResearch {
                it.researchStack(GTLMachines.LIGHTNING_ROD[6].asStack())
                    .dataStack(TOOL_DATA_MODULE.asStack()).dataStack(TOOL_DATA_MODULE.asStack())
                    .EUt(VA[UIV]).CWUt(256)
            }
            .save(provider)

        ASSEMBLY_LINE_RECIPES.recipeBuilder(id("floating_light_deep_space_industrial_vessel"))
            .inputItems(DYSON_SPHERE, 16)
            .inputItems(SPACE_ELEVATOR, 16)
            .inputItems(SPACE_PROBE_SURFACE_RECEPTION, 16)
            .inputItems(ASSEMBLER_MODULE, 32)
            .inputItems(RESOURCE_COLLECTION, 32)
            .inputItems(POWER_MODULE_5, 48)
            .inputItems(CustomTags.UXV_CIRCUITS, 64)
            .inputItems(EXTREMELY_ULTIMATE_BATTERY, 8)
            .inputItems(
                "kubejs:space_drone_mk5".getItemStack(56),
                "kubejs:space_probe_mk1".getItemStack(56),
                "kubejs:precision_circuit_assembly_robot_mk4".getItemStack(56),
                "kubejs:dyson_swarm_module".getItemStack(64),
                "kubejs:dyson_swarm_module".getItemStack(64),
            )
            .inputItems(COVER_SOLAR_PANEL_EV, 64)
            .inputItems(plateDouble, SuperheavyLAlloy, 64)
            .inputItems(plateDouble, SuperheavyHAlloy, 64)
            .inputFluids(SuperMutatedLivingSolder.getFluid(480000))
            .inputFluids(UUMatter.getFluid(560000))
            .inputFluids(Ytterbium178.getFluid(8192))
            .inputFluids(CosmicComputingMixture.getFluid(320000))
            .outputItems(FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL)
            .EUt(VA[UXV].toLong()).duration(7200)
            .stationResearch {
                it.researchStack(DYSON_SPHERE.asStack())
                    .dataStack(TOOL_DATA_MODULE.asStack())
                    .EUt(VA[UXV]).CWUt(2048)
            }
            .save(provider)
    }
}
