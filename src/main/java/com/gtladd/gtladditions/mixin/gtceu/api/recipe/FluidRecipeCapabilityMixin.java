package com.gtladd.gtladditions.mixin.gtceu.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.AbstractMapIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.MapFluidIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.MapFluidTagIngredient;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.world.level.material.Fluid;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(FluidRecipeCapability.class)
@SuppressWarnings("all")
public class FluidRecipeCapabilityMixin {

    private static final Object2ObjectOpenCustomHashMap<FluidStack, List<AbstractMapIngredient>> FluidIngredientMap = new Object2ObjectOpenCustomHashMap<>(FluidStackHashStrategy.comparingAllButAmount());

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public List<AbstractMapIngredient> convertToMapIngredient(Object obj) {
        List<AbstractMapIngredient> ingredients = new ObjectArrayList<>(1);
        if (obj instanceof FluidIngredient ingredient) {
            for (FluidIngredient.Value value : ingredient.values) {
                if (value instanceof FluidIngredient.TagValue tagValue)
                    ingredients.add(new MapFluidTagIngredient(tagValue.getTag()));
                else for (Fluid fluid : value.getFluids())
                    ingredients.add(new MapFluidIngredient(
                            FluidStack.create(fluid, ingredient.getAmount(), ingredient.getNbt())));
            }
        } else if (obj instanceof FluidStack stack) {
            var ingredientList = FluidIngredientMap.get(stack);
            if (ingredientList == null) {
                var list = new ObjectArrayList<AbstractMapIngredient>();
                list.add(new MapFluidIngredient(stack));
                stack.getFluid().builtInRegistryHolder().tags().forEach(tag -> list.add(new MapFluidTagIngredient(tag)));
                FluidIngredientMap.put(stack, list);
                ingredientList = list;
            }
            ingredients.addAll(ingredientList);
        }

        return ingredients;
    }
}
