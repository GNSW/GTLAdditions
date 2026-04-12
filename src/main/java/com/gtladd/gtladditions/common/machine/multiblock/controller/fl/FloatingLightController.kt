package com.gtladd.gtladditions.common.machine.multiblock.controller.fl

import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineHost
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineModule
import org.gtlcore.gtlcore.common.data.GTLMaterials
import org.gtlcore.gtlcore.common.machine.multiblock.part.HugeFluidHatchPartMachine
import org.gtlcore.gtlcore.utils.datastructure.ModuleRenderInfo

import com.gregtechceu.gtceu.api.capability.IEnergyContainer
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine
import com.gregtechceu.gtceu.api.misc.EnergyContainerList
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour
import com.gregtechceu.gtceu.utils.GTUtil

import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
import com.lowdragmc.lowdraglib.side.fluid.FluidStack

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid

import com.gtladd.gtladditions.api.machine.gui.MultiblockDisplayText
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine
import com.gtladd.gtladditions.common.machine.multiblock.controller.fl.FloatingLightPosHelper.calculateModulePositions
import com.gtladd.gtladditions.utils.ComponentUtil.toComponent
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

import java.util.function.Consumer

class FloatingLightController(holder: IMachineBlockEntity) :
    MultiblockControllerMachine(holder),
    IModularMachineHost<FloatingLightController>,
    IFancyUIMachine,
    IDisplayUIMachine,
    IMachineLife {

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

    override fun attachSideTabs(sideTabs: TabsWidget) {
        sideTabs.setMainTab(this)
        CombinedDirectionalFancyConfigurator.of(self(), self())?.let { sideTabs.attachSubTab(it) }
    }

    override fun createUIWidget(): Widget {
        val group = WidgetGroup(0, 0, 182 + 8, 117 + 8)
        group.addWidget(
            DraggableScrollableWidgetGroup(4, 4, 182, 117).setBackground(screenTexture)
                .addWidget(LabelWidget(4, 5, self().blockState.block.descriptionId))
                .addWidget(
                    ComponentPanelWidget(4, 17, ::addDisplayText)
                        .textSupplier(if (this.level!!.isClientSide) null else Consumer(::addDisplayText))
                        .setMaxWidthLimit(150)
                )
        )
        group.setBackground(GuiTextures.BACKGROUND_INVERSE)
        return group
    }

    override fun addDisplayText(textList: MutableList<Component>) {
        MultiblockDisplayText.builder(textList, isFormed)
            .addEnergyTierLine(tier)
            .addComponent("已连接${modulePos.size}个模块".toComponent)
    }

    override fun createUI(entityPlayer: Player): ModularUI = ModularUI(198, 208, this, entityPlayer).widget(FancyMachineUIWidget(this, 198, 208))

    override fun isRemote() = super<MultiblockControllerMachine>.isRemote

    override fun onMachineRemoved() = safeClearModules()

    override fun getModuleSet() = this.modulePos

    override fun getModuleScanPositions(): Array<out BlockPos> = calculateModulePositions(pos, frontFacing)

    override fun isFormed() = this.isFormed

    override fun getMaxModuleCount() = 32

    override fun getModulesForRendering() = listOf(
        ModuleRenderInfo(
            BlockPos(-14, 28, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_1
        ),
        ModuleRenderInfo(
            BlockPos(-14, 28, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_2
        ),
        ModuleRenderInfo(
            BlockPos(14, 28, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_3
        ),
        ModuleRenderInfo(
            BlockPos(14, 28, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_4
        ),
        ModuleRenderInfo(
            BlockPos(-14, 34, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_1
        ),
        ModuleRenderInfo(
            BlockPos(-14, 34, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_2
        ),
        ModuleRenderInfo(
            BlockPos(14, 34, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_3
        ),
        ModuleRenderInfo(
            BlockPos(14, 34, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_4
        ),
        ModuleRenderInfo(
            BlockPos(-14, 40, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_1
        ),
        ModuleRenderInfo(
            BlockPos(-14, 40, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_2
        ),
        ModuleRenderInfo(
            BlockPos(14, 40, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_3
        ),
        ModuleRenderInfo(
            BlockPos(14, 40, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_4
        ),
        ModuleRenderInfo(
            BlockPos(-14, 46, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_1
        ),
        ModuleRenderInfo(
            BlockPos(-14, 46, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_2
        ),
        ModuleRenderInfo(
            BlockPos(14, 46, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_3
        ),
        ModuleRenderInfo(
            BlockPos(14, 46, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_4
        ),
        ModuleRenderInfo(
            BlockPos(-14, 52, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_1
        ),
        ModuleRenderInfo(
            BlockPos(-14, 52, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_2
        ),
        ModuleRenderInfo(
            BlockPos(14, 52, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_3
        ),
        ModuleRenderInfo(
            BlockPos(14, 52, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_4
        ),
        ModuleRenderInfo(
            BlockPos(-14, 58, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_1
        ),
        ModuleRenderInfo(
            BlockPos(-14, 58, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_2
        ),
        ModuleRenderInfo(
            BlockPos(14, 58, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_3
        ),
        ModuleRenderInfo(
            BlockPos(14, 58, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_4
        ),
        ModuleRenderInfo(
            BlockPos(-14, 64, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_1
        ),
        ModuleRenderInfo(
            BlockPos(-14, 64, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_2
        ),
        ModuleRenderInfo(
            BlockPos(14, 64, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_3
        ),
        ModuleRenderInfo(
            BlockPos(14, 64, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_4
        ),
        ModuleRenderInfo(
            BlockPos(-14, 70, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_1
        ),
        ModuleRenderInfo(
            BlockPos(-14, 70, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.WEST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_2
        ),
        ModuleRenderInfo(
            BlockPos(14, 70, 8),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.UP,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_3
        ),
        ModuleRenderInfo(
            BlockPos(14, 70, 12),
            Direction.UP,
            Direction.NORTH,
            Direction.EAST,
            Direction.DOWN,
            MultiBlockMachine.FLOATING_LIGHT_DEEP_SPACE_INDUSTRIAL_VESSEL_MODULE_4
        )
    )

    companion object {
        val RAWSTARMATTER: Fluid = GTLMaterials.RawStarMatter.fluid
    }
}
