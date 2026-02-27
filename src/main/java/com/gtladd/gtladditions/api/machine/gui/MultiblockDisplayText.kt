package com.gtladd.gtladditions.api.machine.gui

import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus

import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.capability.IEnergyContainer
import com.gregtechceu.gtceu.api.recipe.GTRecipeType
import com.gregtechceu.gtceu.utils.FormattingUtil
import com.gregtechceu.gtceu.utils.GTUtil

import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent

import com.gtladd.gtladditions.api.recipe.MultiGTRecipeType
import com.gtladd.gtladditions.utils.ComponentUtil.literal
import com.gtladd.gtladditions.utils.ComponentUtil.toComponent
import com.gtladd.gtladditions.utils.ComponentUtil.translatable
import com.gtladd.gtladditions.utils.MathUtil.maxToLong

object MultiblockDisplayText {
    @JvmStatic
    fun builder(textList: MutableList<Component>, isStructureFormed: Boolean) = Builder(textList, isStructureFormed)

    class Builder(private val textList: MutableList<Component>, private val isStructureFormed: Boolean) {
        private var isWorkingEnabled = false
        private var isActive = false

        init {
            if (!isStructureFormed) {
                val base = "gtceu.multiblock.invalid_structure".toComponent.withStyle(ChatFormatting.RED)
                val hover = "gtceu.multiblock.invalid_structure.tooltip".toComponent.withStyle(ChatFormatting.GRAY)
                textList.add(base.withStyle { it.withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)) })
            }
        }

        fun setWorkingStatus(isWorkingEnabled: Boolean, isActive: Boolean): Builder {
            this.isWorkingEnabled = isWorkingEnabled
            this.isActive = isActive
            return this
        }

        fun addEnergyUsageLine(energyContainer: IEnergyContainer?): Builder {
            if (!isStructureFormed) return this
            energyContainer?.let {
                if (it.energyCapacity > 0) {
                    val mv = it.inputVoltage maxToLong it.outputVoltage
                    val s = FormattingUtil.formatNumbers(mv)
                    val v = GTValues.VNF[GTUtil.getFloorTierByVoltage(mv).toInt()].toComponent
                    val t = Component.translatable("gtceu.multiblock.max_energy_per_tick", s, v).withStyle(ChatFormatting.GRAY)
                    val hoverText = "gtceu.multiblock.max_energy_per_tick_hover".toComponent.withStyle(ChatFormatting.GRAY)
                    textList.add(t.withStyle { style -> style.withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)) })
                }
            }
            return this
        }

        fun addEnergyTierLine(tier: Int): Builder {
            if (!isStructureFormed) return this
            if (tier < GTValues.ULV || tier > GTValues.MAX) return this

            val bodyText = Component.translatable("gtceu.multiblock.max_recipe_tier", GTValues.VNF[tier])
                .withStyle(ChatFormatting.GRAY)
            val hoverText = "gtceu.multiblock.max_recipe_tier_hover".toComponent.withStyle(ChatFormatting.GRAY)
            textList.add(bodyText.withStyle { it.withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)) })
            return this
        }

        fun addWorkingStatusLine(): Builder {
            if (!isStructureFormed) return this
            return if (!isWorkingEnabled) {
                addWorkPausedLine(false)
            } else if (isActive) {
                addRunningPerfectlyLine(false)
            } else {
                addIdlingLine(false)
            }
        }

        fun addWorkPausedLine(checkState: Boolean): Builder {
            takeIf { isStructureFormed && (!checkState || !isWorkingEnabled) }?.let {
                textList.add("gtceu.multiblock.work_paused".toComponent.withStyle(ChatFormatting.GOLD))
            }
            return this
        }

        fun addRunningPerfectlyLine(checkState: Boolean): Builder {
            takeIf { isStructureFormed && (!checkState || isActive) }?.let {
                textList.add("gtceu.multiblock.running".toComponent.withStyle(ChatFormatting.GREEN))
            }
            return this
        }

        fun addIdlingLine(checkState: Boolean): Builder {
            takeIf { isStructureFormed && (!checkState || (isWorkingEnabled && !isActive)) }?.let {
                textList.add("gtceu.multiblock.idling".toComponent.withStyle(ChatFormatting.GRAY))
            }
            return this
        }

        fun addProgressLine(progressPercent: Double): Builder {
            takeIf { isStructureFormed && isActive }?.let {
                textList.add(Component.translatable("gtceu.multiblock.progress", (progressPercent * 100).toInt()))
            }
            return this
        }

        fun addMachineModeLine(recipeType: GTRecipeType): Builder {
            if (!isStructureFormed) return this
            if (recipeType is MultiGTRecipeType) {
                textList.add(
                    Component.translatable(
                        "gtceu.gui.machinemode",
                        "gtceu.multi_recipe.types.0".toComponent
                    ).withStyle(ChatFormatting.AQUA)
                        .append("(").append(recipeType.getTypeList().joinToString(", ") { it.registryName.toLanguageKey().translatable }).append(")")
                )
            } else {
                textList.add(
                    Component.translatable(
                        "gtceu.gui.machinemode",
                        recipeType.registryName.toLanguageKey().translatable
                    ).withStyle(ChatFormatting.AQUA)
                )
            }
            return this
        }

        fun addMachineModesLine(vararg types: GTRecipeType): Builder {
            takeIf { isStructureFormed }?.let {
                textList.add(
                    Component.translatable("gtceu.gui.machinemode", "gtceu.multi_recipe.types.0".translatable)
                        .withStyle(ChatFormatting.AQUA).append("(").append(types.joinToString(", ") { it.registryName.toLanguageKey().translatable }).append(")")
                )
            }
            return this
        }

        fun addParallelsLine(parallel: Number): Builder {
            if (isStructureFormed && parallel.toLong() > 1) {
                val parallelText = FormattingUtil.formatNumbers(parallel).literal.withStyle(ChatFormatting.DARK_PURPLE)
                textList.add(Component.translatable("gtceu.multiblock.parallel", parallelText).withStyle(ChatFormatting.GRAY))
            }
            return this
        }

        fun addRecipeStatus(status: IRecipeStatus): Builder {
            if (!isStructureFormed) return this
            status.recipeStatus?.let { result ->
                result.reason?.let { textList.add(it.copy().withStyle(ChatFormatting.RED)) }
            }
            status.workingStatus?.let { result ->
                result.reason?.let { textList.add(it.copy().withStyle(ChatFormatting.RED)) }
            }
            return this
        }

        fun addButton(component: Component, componentData: String): Builder {
            if (isStructureFormed) textList.add(ComponentPanelWidget.withButton(component, componentData))
            return this
        }

        fun addComponent(vararg components: Component): Builder {
            if (isStructureFormed) components.forEach(textList::add)
            return this
        }
    }
}
