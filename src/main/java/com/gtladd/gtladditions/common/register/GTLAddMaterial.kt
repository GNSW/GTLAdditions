package com.gtladd.gtladditions.common.register

import com.gregtechceu.gtceu.api.data.chemical.material.Material
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet
import com.gregtechceu.gtceu.api.fluids.FluidBuilder
import com.gregtechceu.gtceu.api.fluids.FluidState
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys
import com.gregtechceu.gtceu.common.data.GTMaterials
import com.gregtechceu.gtceu.common.data.GTMaterials.Iridium
import com.gregtechceu.gtceu.common.data.GTMaterials.Ruthenium

import com.gtladd.gtladditions.GTLAdditions

object GTLAddMaterial {
    @JvmField
    val GALLIUM_OXIDE: Material = Material.Builder(GTLAdditions.id("gallium_oxide"))
        .dust().color(15720677)
        .components(GTMaterials.Gallium, 2, GTMaterials.Oxygen, 3)
        .iconSet(MaterialIconSet.DULL)
        .buildAndRegister()

    @JvmField
    val AMMONIUM_GALIUM_SULFATE: Material = Material.Builder(GTLAdditions.id("ammonium_gallium_sulfate"))
        .dust().color(0xFFF6E9)
        .iconSet(MaterialIconSet.DULL)
        .buildAndRegister().setFormula("Ga(NH₄)(SO₄)₂")

    @JvmField
    val MINING_ESSENCE: Material = Material.Builder(GTLAdditions.id("mining_essence"))
        .liquid(FluidBuilder().block()).color(0x835141).buildAndRegister()

    @JvmField
    val TREASURES_ESSENCE: Material = Material.Builder(GTLAdditions.id("treasures_essence"))
        .liquid(FluidBuilder().block()).color(0x9C24FF).buildAndRegister()

    @JvmField
    val CRYSTALLINE_PROTOPLASM: Material = Material.Builder(GTLAdditions.id("crystalline_protoplasm"))
        .liquid(FluidBuilder().block()).color(0x2ECF03).buildAndRegister()

    @JvmField
    val MOLTEN_RURIDIT: Material = Material.Builder(GTLAdditions.id("ruridit"))
        .fluid(FluidStorageKeys.MOLTEN, FluidBuilder().state(FluidState.LIQUID).temperature(2300))
        .color(0x86b3b7)
        .components(Ruthenium, 2, Iridium, 1)
        .flags(MaterialFlags.DISABLE_DECOMPOSITION)
        .iconSet(MaterialIconSet.METALLIC)
        .buildAndRegister()

    @JvmField
    val LIQUID_RURIDIT: Material = Material.Builder(GTLAdditions.id("liquid_ruridit"))
        .fluid(FluidStorageKeys.LIQUID, FluidBuilder().state(FluidState.LIQUID).temperature(2300))
        .components(Ruthenium, 2, Iridium, 1)
        .flags(MaterialFlags.DISABLE_DECOMPOSITION)
        .color(0x86b3b7)
        .buildAndRegister()

    @JvmStatic
    fun init() {}
}
