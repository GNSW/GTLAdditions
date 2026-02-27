package com.gtladd.gtladditions.api.machine.gui

import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.common.data.GTItems

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern
import com.lowdragmc.lowdraglib.gui.texture.*
import com.lowdragmc.lowdraglib.gui.widget.*

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component

import com.gtladd.gtladditions.api.machine.IMultipleRecipeTypeMachine
import com.gtladd.gtladditions.api.recipe.MultiGTRecipeType
import com.gtladd.gtladditions.utils.ComponentUtil.literal
import com.gtladd.gtladditions.utils.ComponentUtil.translatable
import com.gtladd.gtladditions.utils.MathUtil.minToInt

class GTLAddMachineModeFancyConfigurator(private var machine: IMultipleRecipeTypeMachine) : IFancyUIProvider {

    override fun getTitle(): Component = Component.translatable("gtceu.gui.machinemode.title")

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(GTItems.ROBOT_ARM_LV.get())

    override fun createMainPage(widget: FancyMachineUIWidget): Widget {
        val length = machine.multiRecipeTypes.size
        val group = MachineModeConfigurator(0, 0, 140, 20 * (length minToInt 6) + 4)
        group.setBackground(GuiTextures.BACKGROUND_INVERSE)
        if (length > 6) {
            val widgetGroup = DraggableScrollableWidgetGroup(2, 2, 136, 6 * 20)
            addWidgets(widgetGroup, length)
            group.addWidget(widgetGroup)
        } else {
            addWidgets(group, length)
        }
        return group
    }

    private fun addWidgets(group: WidgetGroup, length: Int) {
        val x = if (length < 7) 2 else 0
        for (i in 0..<length) {
            val finalI = i
            val type = machine.multiRecipeTypes[finalI]
            group.addWidget(ButtonWidget(x, x + i * 20, 136, 20, IGuiTexture.EMPTY) { machine.activeRecipeType = finalI })
            group.addWidget(
                ImageWidget(x, x + i * 20, 136, 20) {
                    GuiTextureGroup(
                        ResourceBorderTexture.BUTTON_COMMON.copy()
                            .setColor(if (machine.activeRecipeType == finalI) ColorPattern.CYAN.color else -1),
                        TextTexture((type as? MultiGTRecipeType)?.getTypeList()?.joinToString(", ") { it.registryName.toLanguageKey().translatable } ?: type.registryName.toLanguageKey().translatable)
                            .setWidth(136).setType(TextTexture.TextType.ROLL)
                    )
                }
            )
        }
    }

    override fun getTabTooltips() = listOf("Change active Machine Mode".literal)

    inner class MachineModeConfigurator(x: Int, y: Int, width: Int, height: Int) : WidgetGroup(x, y, width, height) {
        override fun writeInitialData(buffer: FriendlyByteBuf) {
            buffer.writeVarInt(machine.activeRecipeType)
        }

        override fun readInitialData(buffer: FriendlyByteBuf) {
            machine.activeRecipeType = buffer.readVarInt()
        }

        override fun detectAndSendChanges() {
            this.writeUpdateInfo(0) { it.writeVarInt(machine.activeRecipeType) }
        }

        override fun readUpdateInfo(id: Int, buffer: FriendlyByteBuf) {
            if (id == 0) machine.activeRecipeType = buffer.readVarInt()
        }
    }
}
