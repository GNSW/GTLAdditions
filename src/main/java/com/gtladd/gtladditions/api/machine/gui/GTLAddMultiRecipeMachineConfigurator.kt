package com.gtladd.gtladditions.api.machine.gui

import org.gtlcore.gtlcore.api.gui.ExtendLabelWidget

import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup

import net.minecraft.network.chat.Component

import com.gtladd.gtladditions.api.machine.IGTLAddMachine
import com.gtladd.gtladditions.utils.ComponentUtil.toComponent
import com.hepdd.gtmthings.GTMThings

class GTLAddMultiRecipeMachineConfigurator(private val machine: IGTLAddMachine) : IFancyConfigurator {
    override fun getTitle() = "gtceu.machine.multi_recipe.mode.configurator".toComponent

    override fun getIcon() = ResourceTexture(GTMThings.id("textures/item/opv_4a_wireless_energy_receive_cover.png"))

    override fun createConfigurator(): Widget {
        if (machine.useModes()) {
            val label = ExtendLabelWidget(
                28,
                40,
                Component.translatable("gtceu.machine.multiple_recipe.gui." + if (machine.isMultipleMode) "0" else "1")
            )
            return WidgetGroup(0, 0, 100, 60)
                .addWidget(
                    ExtendLabelWidget(
                        4,
                        0,
                        "gtceu.machine.limit_duration_configurator".toComponent
                    )
                )
                .addWidget(
                    IntInputWidget(
                        4,
                        12,
                        90,
                        20,
                        { machine.limitedDuration },
                        { machine.limitedDuration = it }
                    ).setMin(5).setMax(200)
                )
                .addWidget(
                    SwitchWidget(4, 35, 20, 20) { cd, p ->
                        machine.isMultipleMode = p
                        label.setComponent(Component.translatable("gtceu.machine.multiple_recipe.gui." + if (p) "0" else "1"))
                    }
                        .setTexture(GuiTextureGroup(ExtremeMode), GuiTextureGroup(DividedMode))
                        .setPressed(machine.isMultipleMode)
                ).addWidget(label)
        } else {
            return WidgetGroup(0, 0, 100, 20)
                .addWidget(
                    IntInputWidget(
                        { machine.limitedDuration },
                        { machine.limitedDuration = it }
                    ).setMin(5).setMax(200)
                )
        }
    }

    companion object {
        val DividedMode = ResourceTexture("gtladditions:textures/gui/equally_divided.png")
        val ExtremeMode = ResourceTexture("gtladditions:textures/gui/extreme.png")
    }
}
