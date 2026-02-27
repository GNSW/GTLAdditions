package com.gtladd.gtladditions.mixin.gtceu.integration.jade;

import org.gtlcore.gtlcore.utils.Registries;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.integration.jade.provider.RecipeOutputProvider;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import com.gtladd.gtladditions.utils.GTRecipeUtils;
import com.gtladd.gtladditions.utils.O2LHashMap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = RecipeOutputProvider.class, priority = 1100)
public abstract class RecipeOutputProviderMixin extends CapabilityBlockProvider<RecipeLogic> {

    protected RecipeOutputProviderMixin(ResourceLocation uid) {
        super(uid);
    }

    @Override
    protected void write(CompoundTag data, RecipeLogic recipeLogic) {
        if (recipeLogic.isWorking()) {
            data.putBoolean("Working", recipeLogic.isWorking());
            var r = recipeLogic.getLastRecipe();
            if (r != null) {
                var items = new O2LHashMap<>(ItemStackHashStrategy.comparingAllButCount());
                var fluids = new O2LHashMap<>(FluidStackHashStrategy.comparingAllButAmount());

                for (var entry : r.outputs.entrySet()) {
                    if (entry.getKey() == ItemRecipeCapability.CAP) {
                        entry.getValue().forEach(c -> {
                            var i = (Ingredient) c.content;
                            items.addTo(i.kjs$getFirst(), GTRecipeUtils.INSTANCE.getAmount(i));
                        });
                    } else if (entry.getKey() == FluidRecipeCapability.CAP) {
                        entry.getValue().forEach(c -> {
                            var i = (FluidIngredient) c.content;
                            fluids.addTo(i.getStacks()[0], i.getAmount());
                        });
                    }
                }

                if (!items.isEmpty()) {
                    var itemTags = new ListTag();
                    items.object2LongEntrySet().fastForEach((c) -> {
                        var nbt = new CompoundTag();
                        var s = c.getKey();
                        if (!s.isEmpty()) {
                            nbt.putString("id", Registries.getItemId(s.getItem()));
                            nbt.putLong("Count", c.getLongValue());
                            if (s.getTag() != null) nbt.putString("tag", s.getTag().toString());
                            itemTags.add(nbt);
                        }
                    });
                    data.put("OutputItems", itemTags);
                }
                if (!fluids.isEmpty()) {
                    var fluidTags = new ListTag();
                    fluids.object2LongEntrySet().fastForEach((c) -> {
                        var nbt = new CompoundTag();
                        var f = c.getKey();
                        if (f != null) {
                            nbt.putString("FluidName", Registries.getFluidId(f));
                            nbt.putLong("Amount", c.getLongValue());
                            if (f.getTag() != null) nbt.putString("Tag", f.getTag().toString());
                            fluidTags.add(nbt);
                        }
                    });
                    data.put("OutputFluids", fluidTags);
                }
            }
        }
    }
}
