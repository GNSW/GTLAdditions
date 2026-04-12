package com.gtladd.gtladditions.common.machine.multiblock.controller.fl

import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineHost
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineModule
import org.gtlcore.gtlcore.common.data.GTLMaterials
import org.gtlcore.gtlcore.common.machine.multiblock.part.HugeFluidHatchPartMachine
import org.gtlcore.gtlcore.utils.datastructure.ModuleRenderInfo

import com.gregtechceu.gtceu.api.capability.IEnergyContainer
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine
import com.gregtechceu.gtceu.api.misc.EnergyContainerList
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour
import com.gregtechceu.gtceu.utils.GTUtil

import com.lowdragmc.lowdraglib.side.fluid.FluidStack

import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid

import com.gtladd.gtladditions.utils.FloatingLightPosHelper.calculateModulePositions
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

class FloatingLightController(holder: IMachineBlockEntity) :
    MultiblockControllerMachine(holder), IModularMachineHost<FloatingLightController>, IMachineLife {

    val modulePos = ObjectOpenHashSet<IModularMachineModule<FloatingLightController, FloatingLightModule>>(32)
    private val energyTick = ConditionalSubscriptionHandler(this, {
        val energyContainer = getEnergy()
        energyContainer.removeEnergy(energyContainer.inputVoltage)
        if (getCircuit() == 2 && offsetTimer % 20 == 0L) {
            val fluid = fluidHatch?.tank?.getFluidInTank(0) ?: FluidStack.empty()
            if (!fluid.isEmpty && fluid.fluid == RAWSTARMATTER && fluid.amount >= 1000) {
                fluidHatch?.tank?.drainInternal(1000, false)
                isDouble = true
            } else {
                isDouble = false
            }
        }
    }) { this.isFormed }

    private var fluidHatch: HugeFluidHatchPartMachine? = null
    private var energyHatch: EnergyContainerList? = null
    var isDouble = false
    var tier = 0

    fun getCircuit() = IntCircuitBehaviour.getCircuitConfiguration(fluidHatch?.circuitInventory?.getStackInSlot(0) ?: ItemStack.EMPTY)

    fun getEnergy(): EnergyContainerList {
        if (energyHatch == null) {
            val list = ObjectArrayList<IEnergyContainer>()
            parts.forEach { it.recipeHandlers.forEach { i -> if (i is IEnergyContainer) list.add(i) } }
            this.energyHatch = EnergyContainerList(list)
        }
        return energyHatch!!
    }

    override fun onStructureFormed() {
        isFormed = true
        this.parts.clear()
        val set = multiblockState.matchContext.getOrCreate<MutableSet<IMultiPart>>("parts") { ObjectOpenHashSet() }
        for (part in set) {
            this.parts.add(part)
            part.addedToController(this)
            if (part is HugeFluidHatchPartMachine) this.fluidHatch = part
        }
        updatePartPositions()
        safeClearModules()
        scanAndConnectModules()
        tier = GTUtil.getFloorTierByVoltage(getEnergy().highestInputVoltage).toInt()
        energyTick.initialize(level)
    }

    override fun onStructureInvalid() {
        isFormed = false
        parts.forEach { it.removedFromController(this) }
        parts.clear()
        updatePartPositions()
        energyHatch = null
        tier = 0
        safeClearModules()
    }

    override fun onMachineRemoved() = safeClearModules()

    override fun getModuleSet() = this.modulePos

    override fun getModuleScanPositions(): Array<out BlockPos> = calculateModulePositions(pos, frontFacing)

    override fun isFormed() = this.isFormed

    override fun getMaxModuleCount() = 32

    override fun getModulesForRendering(): List<ModuleRenderInfo> {
        TODO("Not yet implemented")
    }

    companion object {
        val RAWSTARMATTER: Fluid = GTLMaterials.RawStarMatter.fluid
    }
}
