package com.gtladd.gtladditions.mixin.gtlcore.machine.part;

import org.gtlcore.gtlcore.common.machine.multiblock.part.LargeSteamHatchPartMachine;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import com.gtladd.gtladditions.common.machine.hatch.HugeSteamHatchPartMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(LargeSteamHatchPartMachine.class)
public class LargeSteamHatchPartMachineMixin {

    @ModifyArg(method = "createTank",
               at = @At(value = "INVOKE",
                        target = "Lcom/gregtechceu/gtceu/api/machine/trait/NotifiableFluidTank;setFilter(Ljava/util/function/Predicate;)Lcom/gregtechceu/gtceu/api/machine/trait/NotifiableFluidTank;"),
               remap = false)
    protected Predicate<FluidStack> createTank(Predicate<FluidStack> filter) {
        return fluidStack -> fluidStack.getFluid().is(HugeSteamHatchPartMachine.Companion.getTag());
    }
}
