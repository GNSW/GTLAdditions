package com.gtladd.gtladditions.mixin.gtlcore.machine;

import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.HarmonyMachine;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import com.gtladd.gtladditions.common.saved.HarmonySaved;
import com.gtladd.gtladditions.utils.MachineUtil;
import com.llamalad7.mixinextras.sugar.Local;
import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.Overwrite;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
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

    @Persisted
    private final NotifiableItemStackHandler machineStorage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH,
            slots -> new ItemStackTransfer(1) {

                @Override
                public int getSlotLimit(int slot) {
                    return 1;
                }
            });

    @Redirect(method = "recipeModifier",
              at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/recipe/GTRecipe;copy()Lcom/gregtechceu/gtceu/api/recipe/GTRecipe;"),
              remap = false)
    private static @NotNull GTRecipe modify(GTRecipe instance, @Local(name = "machine") MetaMachine machine) {
        val hm = (HarmonyMachineMixin) machine;
        ItemStack machineStorageItem = hm.machineStorage.storage.getStackInSlot(0);
        boolean hasUltimateTea = false;
        if (!machineStorageItem.isEmpty()) {
            String itemId = BuiltInRegistries.ITEM.getKey(machineStorageItem.getItem()).toString();
            hasUltimateTea = "gtladditions:create_data".equals(itemId);
        }
        if (hasUltimateTea) {
            GTRecipe modified = new GTRecipe(
                    instance.recipeType,
                    instance.id,
                    instance.inputs,
                    instance.outputs,
                    instance.tickInputs,
                    instance.tickOutputs,
                    instance.inputChanceLogics,
                    instance.outputChanceLogics,
                    instance.tickInputChanceLogics,
                    instance.tickOutputChanceLogics,
                    instance.conditions,
                    instance.ingredientActions,
                    instance.data,
                    instance.duration,
                    instance.isFuel);
            modified.outputs.clear();
            RecipeCapability<FluidIngredient> fluidCap = FluidRecipeCapability.CAP;
            Content fluidContent = new Content(FluidIngredient.of(GTLMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 1310720 * 12)), 10000, 10000, 0, null, null);
            modified.outputs.put(fluidCap, List.of(fluidContent));
            return modified;
        } else {
            return instance.copy();
        }
    }

    @Override
    public @NotNull Widget createUIWidget() {
        WidgetGroup group = (WidgetGroup) super.createUIWidget();
        SlotWidget slot = new SlotWidget(
                machineStorage,
                0,
                group.getSizeWidth() - 30,
                group.getSizeHeight() - 30,
                true,
                true);
        slot.setBackground(GuiTextures.SLOT);
        group.addWidget(slot);
        return group;
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
}
