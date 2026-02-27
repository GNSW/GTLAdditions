package com.gtladd.gtladditions.common.machine.muiltblock.controller.df

import org.gtlcore.gtlcore.common.data.GTLMaterials

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient
import com.gregtechceu.gtceu.common.data.GTMaterials
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine

import com.lowdragmc.lowdraglib.side.fluid.FluidStack
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder

import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluids

import com.gtladd.gtladditions.api.machine.gui.MultiblockDisplayText
import com.gtladd.gtladditions.api.recipe.ContentList
import com.gtladd.gtladditions.api.recipe.ContentList.MaxChanceContent
import com.gtladd.gtladditions.common.data.RecipesModify
import com.gtladd.gtladditions.common.machine.hatch.VientianeTranscriptionNode
import com.gtladd.gtladditions.common.machine.muiltblock.controller.Resource
import com.gtladd.gtladditions.utils.ComponentUtil.toComponent
import com.gtladd.gtladditions.utils.GTRecipeUtils.create
import com.gtladd.gtladditions.utils.MathUtil.maxToInt
import com.gtladd.gtladditions.utils.MathUtil.minToDouble
import com.gtladd.gtladditions.utils.MathUtil.minToInt
import com.gtladd.gtladditions.utils.MathUtil.minToLong
import com.gtladd.gtladditions.utils.MathUtil.pow

import kotlin.math.exp
import kotlin.math.pow

class ReverseTimeBoostingEngine(holder: IMachineBlockEntity) : RRFModuleMachine(holder) {

    private var fluidHatch: FluidHatchPartMachine? = null
    private var itemHatch: ItemBusPartMachine? = null
    private var hatch: VientianeTranscriptionNode? = null

    @Persisted
    var superHeat = false

    @Persisted
    private var temperature = 48000
    private var returnItem: ItemStack? = null
    private var returnFluid: FluidStack? = null

    fun modifyRecipe(recipe: GTRecipe): GTRecipe? {
        if (superHeat) return null
        if (isWorking) {
            val div = findDiv(recipe)
            if (div > 0.1) recipe.duration = (recipe.duration * (0.8 minToDouble (0.05 + 0.7932 * exp(-0.8473 * div.pow(2.326))))) maxToInt 1
        }
        return recipe
    }

    fun setReturnContent(recipe: GTRecipe) {
        if (returnItem != null) {
            recipe.outputs.computeIfAbsent(ItemRecipeCapability.CAP) { ContentList() }.add(MaxChanceContent(returnItem!!.create()))
            returnItem = null
            return
        }
        if (returnFluid != null) {
            recipe.outputs.computeIfAbsent(FluidRecipeCapability.CAP) { ContentList() }.add(MaxChanceContent(FluidIngredient.of(returnFluid!!)))
            returnFluid = null
            return
        }
    }

    private fun findDiv(recipe: GTRecipe): Double {
        itemHatch?.inventory?.let {
            RecipesModify.RTBERecipeItemMap[recipe.id.hashCode()]?.let { t ->
                for (i in 0..<it.storage.slots) {
                    val item = it.storage.getStackInSlot(i)
                    if (!item.isEmpty) {
                        for (il in t) if (il.first() == item.item) {
                            val ma = temperature * il.rightInt()
                            it.extractItemInternal(i, item.count minToInt il.rightInt(), false)
                            returnItem = item.copyWithCount((getReturnDiv() * (item.count minToInt ma)).toInt())
                            return (item.count / ma) minToDouble 1
                        }
                    }
                }
            }
        }
        fluidHatch?.tank?.let {
            RecipesModify.RTBERecipeFluidMap[recipe.id.hashCode()]?.let { t ->
                for (i in 0..<it.storages.size) {
                    val fluid = it.storages[i].fluid
                    if (!fluid.isEmpty) {
                        for (fl in t) if (fl.first() == fluid.fluid) {
                            val ma = temperature * fl.rightInt().toLong()
                            it.drainInternal(fluid.copy(fluid.amount minToLong fl.rightInt()), false)
                            returnFluid = fluid.copy((getReturnDiv() * (fluid.amount minToLong ma)).toLong())
                            return (fluid.amount / ma) minToDouble 1
                        }
                    }
                }
            }
        }
        return 0.0
    }

    private fun getReturnDiv() = if (temperature > 97000) {
        1.0 - 0.85 * ((temperature - 97000) / 13000).pow(0.42)
    } else if (temperature >= 93000) {
        1.0
    } else if (temperature >= 48000) {
        0.5 + 0.5 * (temperature - 48000) / 45000
    } else {
        0.0
    }

    override fun startupUpdate() {
        if (offsetTimer % 20 == 0L) {
            fluidHatch?.tank?.let {
                it.contents.forEach { f ->
                    val fluidStack = f as FluidStack
                    if (fluidStack.amount >= 100000) {
                        when (fluidStack.fluid) {
                            LAVA.fluid -> if (!superHeat) {
                                safePlusTemperature(2500)
                                it.drainInternal(LAVA, false)
                            }
                            BLAZE.fluid -> if (!superHeat) {
                                safePlusTemperature(4600)
                                it.drainInternal(BLAZE, false)
                            }
                            RAW_STAR_MATTER_PLASMA.fluid -> if (!superHeat) {
                                safePlusTemperature(14000)
                                it.drainInternal(RAW_STAR_MATTER_PLASMA, false)
                            }
                            ICE.fluid -> {
                                safeMinusTemperature(1900 + if (superHeat) 7125 else 0)
                                it.drainInternal(ICE, false)
                            }
                            HELIUM.fluid -> {
                                safeMinusTemperature(3400 + if (superHeat) 7125 else 0)
                                it.drainInternal(HELIUM, false)
                            }
                            CRYOTHEUM.fluid -> {
                                safeMinusTemperature(6700 + if (superHeat) 7125 else 0)
                                it.drainInternal(CRYOTHEUM, false)
                            }
                        }
                    }
                }
            }
        }
    }

    fun safePlusTemperature(plus: Int) {
        temperature += plus
        hatch?.update(temperature)
        if (temperature > 105000) superHeat = true
    }

    fun safeMinusTemperature(minus: Int) {
        temperature -= minus
        if (temperature < 48000) {
            temperature = 48000
            if (superHeat) superHeat = false
        }
        hatch?.update(temperature)
    }

    override fun addDisplayText(textList: MutableList<Component>) {
        MultiblockDisplayText.builder(textList, isFormed())
            .addComponent(
                Component.translatable("gtceu.machine.recursive_reverse_forge.gui.module.4", if (host == null) "×" else "✓"),
                Component.translatable("gtceu.machine.reverse_time_boosting_engine.gui.tooltip.0", this.temperature),
                Component.translatable("gtceu.machine.reverse_time_boosting_engine.gui.tooltip.1", if (superHeat) "Yes" else "No"),
                if (isWorking) "gtceu.multiblock.running".toComponent else "gtceu.multiblock.work_paused".toComponent
            )
    }

    override fun partTest(part: IMultiPart) {
        when (part) {
            is VientianeTranscriptionNode -> {
                this.hatch = part
                this.hatch!!.controlMachine = true
            }
            is FluidHatchPartMachine -> this.fluidHatch = part
            is ItemBusPartMachine -> this.itemHatch = part
        }
    }

    override fun onStructureInvalid() {
        super.onStructureInvalid()
        this.hatch!!.controlMachine = false
        this.hatch = null
    }

    override fun getFieldHolder() = MANAGED_FIELD_HOLDER

    companion object {
        val MANAGED_FIELD_HOLDER = ManagedFieldHolder(ReverseTimeBoostingEngine::class.java, RRFModuleMachine.MANAGED_FIELD_HOLDER)
        private val LAVA = FluidStack.create(Fluids.LAVA, 100000)
        private val BLAZE = GTMaterials.Blaze.getFluid(100000)
        private val RAW_STAR_MATTER_PLASMA = GTLMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 100000)
        private val ICE = GTMaterials.Ice.getFluid(100000)
        private val HELIUM = GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000)
        private val CRYOTHEUM = FluidStack.create(Resource.Cryotheum, 100000)
    }
}
