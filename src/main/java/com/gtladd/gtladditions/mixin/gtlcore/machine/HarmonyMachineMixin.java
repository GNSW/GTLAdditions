package com.gtladd.gtladditions.mixin.gtlcore.machine;

import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.HarmonyMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import com.gtladd.gtladditions.common.saved.HarmonySaved;
import com.gtladd.gtladditions.utils.MachineUtil;
import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.Overwrite;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

import static org.gtlcore.gtlcore.utils.MachineIO.inputFluid;

@Mixin(HarmonyMachine.class)
public class HarmonyMachineMixin extends NoEnergyMultiblockMachine implements IMachineLife {

    private static final FluidStack Helium = GTMaterials.Helium.getFluid(100000000L);
    private static final FluidStack Hydrogen = GTMaterials.Hydrogen.getFluid(100000000L);

    @Shadow(remap = false)
    private int oc = 0;
    @Shadow(remap = false)
    private UUID userid;
    @Shadow(remap = false)
    private long hydrogen = 0L;
    @Shadow(remap = false)
    private long helium = 0L;

    public HarmonyMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Overwrite(remap = false)
    protected void StartupUpdate() {
        if (this.getOffsetTimer() % 20L == 0L) {
            this.oc = 0;
            if (this.hydrogen < 10000000000L && inputFluid(this, Hydrogen)) this.hydrogen += 100000000L;
            if (this.helium < 10000000000L && inputFluid(this, Helium)) this.helium += 100000000L;
            this.oc = Mth.clamp(MachineUtil.INSTANCE.getCircuit(this), 0, 4);
        }
    }

    @Override
    public void onMachineRemoved() {
        HarmonySaved.Companion.getINSTANCE().remove(this.getPos().asLong());
    }

    @Override
    public void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {
        if (player != null) this.userid = player.getUUID();
        HarmonySaved.Companion.getINSTANCE().update(this.getPos().asLong());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        HarmonySaved.Companion.getINSTANCE().update(this.getPos().asLong());
    }

    @Override
    public void onUnload() {
        super.onUnload();
        HarmonySaved.Companion.getINSTANCE().remove(this.getPos().asLong());
    }
}
