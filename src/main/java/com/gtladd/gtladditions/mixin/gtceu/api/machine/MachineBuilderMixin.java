package com.gtladd.gtladditions.mixin.gtceu.api.machine;

import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;

import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = MachineBuilder.class, priority = 900)
public class MachineBuilderMixin {

    private static final Component[] tooltips = new Component[] {
            Component.translatable("gtceu.universal.enabled").append("(")
                    .append(Component.translatable("gui.gtladditions.modify").append(")")),
            Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
    };

    @Shadow(remap = false)
    @Final
    protected String name;

    @ModifyArg(method = "tooltips", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;stream([Ljava/lang/Object;)Ljava/util/stream/Stream;"), remap = false)
    public <T> T[] tooltips(T[] array) {
        if (containsAnyKeyword(name)) {
            if (array.length > 1) array[0] = (T) tooltips[0];
            else array = (T[]) tooltips;
        }
        return array;
    }

    private boolean containsAnyKeyword(String text) {
        String[] keywords = {
                "auto_configuration_maintenance_hatch",
                "cleaning_configuration_maintenance_hatch",
                "sterile_configuration_cleaning_maintenance_hatch",
                "law_configuration_cleaning_maintenance_hatch",
                "gravity_configuration_hatch",
                "cleaning_gravity_configuration_maintenance_hatch",
                "sterile_cleaning_gravity_configuration_maintenance_hatch",
                "law_cleaning_gravity_configuration_maintenance_hatch"
        };

        for (String keyword : keywords) if (text.contains(keyword)) return true;
        return false;
    }
}
