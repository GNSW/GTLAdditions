package com.gtladd.gtladditions.common.machine.multiblock.controller

import net.minecraft.world.item.Item

import com.gtladd.gtladditions.utils.Registries.getFluid
import com.gtladd.gtladditions.utils.Registries.getItem

object Resource {
    val Cryotheum = "kubejs:gelid_cryotheum".getFluid
    val HyperdimensionalDrone = "kubejs:hyperdimensional_drone".getItem
    val BlackBodyNaquadriaSupersolid = "kubejs:black_body_naquadria_supersolid".getItem
    val HyperStableSelfHealingAdhesive = "kubejs:hyper_stable_self_healing_adhesive".getItem
    val QuantumAnomaly: Item = "kubejs:quantum_anomaly".getItem
    val Hypercube: Item = "kubejs:hypercube".getItem
    val CryotheumDust: Item = "kubejs:dust_cryotheum".getItem
    val CreateUltimateBattery: Item = "kubejs:create_ultimate_battery".getItem
    val ExtremelyDurablePlasmaCell = "kubejs:extremely_durable_plasma_cell".getItem
    val TimeDilationContainmentUnit = "kubejs:time_dilation_containment_unit".getItem
    val PlasmaContainmentCell = "kubejs:plasma_containment_cell".getItem
    val CosmicNeutronPlasmaCell = "kubejs:cosmic_neutron_plasma_cell".getItem
    val ContainedHighDensityProtonicMatter = "kubejs:contained_high_density_protonic_matter".getItem
    val NeutronPlasmaContainmentCell = "kubejs:neutron_plasma_containment_cell".getItem
    val ContainedExoticMatter = "kubejs:contained_exotic_matter".getItem
    val ContainedReissnerNordstromSingularity = "kubejs:contained_reissner_nordstrom_singularity".getItem

    val cellSet = setOf(ExtremelyDurablePlasmaCell, TimeDilationContainmentUnit, PlasmaContainmentCell)
    val itemSet = setOf(
        CosmicNeutronPlasmaCell,
        ContainedHighDensityProtonicMatter,
        NeutronPlasmaContainmentCell,
        ContainedExoticMatter,
        ContainedReissnerNordstromSingularity
    )
}
