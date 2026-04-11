package com.gtladd.gtladditions.mixin.gtceu.api.machine;

import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;

import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Arrays;

@Mixin(value = MachineBuilder.class, priority = 900)
public class MachineBuilderMixin {

    @Unique
    private static final Component tooltips = Component.translatable("gtceu.universal.enabled").append("(").append(Component.translatable("gui.gtladditions.modify").append(")"));

    @Unique
    private static final String[] keyWords = {
            "auto_configuration_maintenance_hatch",
            "cleaning_configuration_maintenance_hatch",
            "sterile_configuration_cleaning_maintenance_hatch",
            "law_configuration_cleaning_maintenance_hatch",
            "gravity_configuration_hatch",
            "cleaning_gravity_configuration_maintenance_hatch",
            "sterile_cleaning_gravity_configuration_maintenance_hatch",
            "law_cleaning_gravity_configuration_maintenance_hatch"
    };

    @Shadow(remap = false)
    @Final
    protected String name;

    @ModifyArg(method = "tooltips", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;stream([Ljava/lang/Object;)Ljava/util/stream/Stream;"), remap = false)
    public <T> T[] tooltips(T[] array) {
        if (Arrays.stream(keyWords).anyMatch(name::contains)) array[0] = (T) tooltips;
        return array;
    }
}
