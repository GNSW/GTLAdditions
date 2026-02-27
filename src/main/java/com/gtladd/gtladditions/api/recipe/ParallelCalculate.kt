package com.gtladd.gtladditions.api.recipe

import org.gtlcore.gtlcore.api.machine.multiblock.ParallelMachine
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine
import org.gtlcore.gtlcore.api.machine.trait.MEPatternRecipeHandlePart
import org.gtlcore.gtlcore.api.machine.trait.RecipeHandlePart
import org.gtlcore.gtlcore.api.recipe.IParallelLogic

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy.comparingAllButAmount
import com.gregtechceu.gtceu.utils.IngredientEquality.IngredientHashStrategy.INSTANCE
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy.comparingAllButCount

import com.lowdragmc.lowdraglib.side.fluid.FluidStack

import net.minecraft.world.item.crafting.Ingredient

import com.gtladd.gtladditions.utils.GTRecipeUtils.amount
import com.gtladd.gtladditions.utils.MathUtil.minToLong
import com.gtladd.gtladditions.utils.O2LHashMap
import com.hepdd.gtmthings.common.block.machine.trait.CatalystFluidStackHandler
import it.unimi.dsi.fastutil.objects.Object2LongMap
import it.unimi.dsi.fastutil.objects.Object2LongMaps
import it.unimi.dsi.fastutil.objects.Object2LongMaps.fastIterable
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap

import java.util.function.Predicate

object ParallelCalculate {

    fun getParallel(holder: IRecipeLogicMachine, recipe: GTRecipe, parallelAmount: Long): Long {
        if (parallelAmount <= 1) return parallelAmount
        val maxParallel = getMaxParallel(holder, recipe, parallelAmount)
        if (maxParallel == 0L) return 0L
        return IParallelLogic.getMinParallel(holder, recipe, maxParallel)
    }

    fun getMaxParallel(holder: IRecipeLogicMachine, recipe: GTRecipe, parallelAmount: Long): Long {
        var amount = parallelAmount
        for (cap in recipe.inputs.keys) {
            if (cap === ItemRecipeCapability.CAP) {
                amount = amount minToLong getInputItemParallel(holder, recipe, amount)
                if (amount == 0L) break
            } else if (cap === FluidRecipeCapability.CAP) {
                amount = amount minToLong getInputFluidParallel(holder, recipe, amount)
                if (amount == 0L) break
            }
        }
        return amount
    }

    fun getInputItemParallel(holder: IRecipeLogicMachine, recipe: GTRecipe, parallelAmount: Long): Long {
        if (parallelAmount <= 1) return parallelAmount
        if (holder !is IRecipeCapabilityMachine) return 1
        if (holder.emptyRecipeHandlePart()) return 0

        val countableMap = Object2LongOpenCustomHashMap(INSTANCE)
        val confirmMEStock = holder is ParallelMachine && holder.needConfirmMEStock()

        for (content in recipe.getInputContents(ItemRecipeCapability.CAP)) {
            if (content.chance <= 0) continue
            val k = content.content as Ingredient
            countableMap.addTo(k, k.amount)
        }

        if (countableMap.isEmpty()) return parallelAmount

        val ingredientStacks = O2LHashMap(comparingAllButCount())

        val handle = holder.getActiveRecipeHandle(recipe)
        when (handle) {
            is MEPatternRecipeHandlePart -> fastIterable(handle.getMEContent(ItemRecipeCapability.CAP, recipe)).forEach { (i, l) -> ingredientStacks.addTo(i, l) }
            is RecipeHandlePart -> fastIterable(handle.getSelfContent(ItemRecipeCapability.CAP, confirmMEStock)).forEach { (i, l) -> ingredientStacks.addTo(i, l) }
            else -> holder.sharedRecipeHandlePart?.let { fastIterable(it.getSelfContent(ItemRecipeCapability.CAP, confirmMEStock)).forEach { (i, l) -> ingredientStacks.addTo(i, l) } }
        }
        return calculate(parallelAmount, countableMap, ingredientStacks)
    }

    fun getInputFluidParallel(holder: IRecipeLogicMachine, recipe: GTRecipe, parallelAmount: Long): Long {
        if (parallelAmount <= 1) return parallelAmount
        if (holder !is IRecipeCapabilityMachine) return 1
        if (holder.emptyRecipeHandlePart()) return 0

        val fluidCountMap = Object2LongOpenHashMap<FluidIngredient>()
        val confirmMEStock = holder is ParallelMachine && holder.needConfirmMEStock()

        for (content in recipe.getInputContents(FluidRecipeCapability.CAP)) {
            val k = content.content as FluidIngredient
            if (content.chance > 0) fluidCountMap.addTo(k, k.amount)
        }

        if (fluidCountMap.isEmpty()) return parallelAmount

        val ingredientStacks = O2LHashMap(comparingAllButAmount())

        val handle = holder.getActiveRecipeHandle(recipe)
        when (handle) {
            is MEPatternRecipeHandlePart -> fastIterable(handle.getMEContent(FluidRecipeCapability.CAP, recipe)).forEach { (f, l) -> ingredientStacks.addTo(f, l) }
            is RecipeHandlePart -> fastIterable(
                if (holder.isDistinct) {
                    handle.getSelfContent(FluidRecipeCapability.CAP, confirmMEStock)
                } else {
                    handle.getContentWithShared(FluidRecipeCapability.CAP, confirmMEStock)
                }
            ).forEach { (f, l) -> ingredientStacks.addTo(f, l) }
            else -> holder.sharedRecipeHandlePart?.let {
                for (handler in it.getCapability(FluidRecipeCapability.CAP)) {
                    if (handler is CatalystFluidStackHandler) continue
                    for (o in handler.contents) if (o is FluidStack) ingredientStacks.addTo(o, o.amount)
                }
            }
        }
        return calculate(parallelAmount, fluidCountMap, ingredientStacks)
    }

    private fun <I : Predicate<S>, S> calculate(parallelLimit: Long, countableMap: Object2LongMap<I>, ingredientStacks: Object2LongMap<S>): Long {
        var parallelLimit = parallelLimit
        if (ingredientStacks.isEmpty()) return 0

        for ((k, v) in fastIterable<I>(countableMap)) {
            var available: Long = 0

            val it = Object2LongMaps.fastIterator(ingredientStacks)
            while (it.hasNext()) {
                val input = it.next()
                if (k.test(input.key)) {
                    available = input.longValue
                    it.remove()
                    break
                }
            }
            if (available < v) return 0
            parallelLimit = parallelLimit minToLong (available / v)
        }
        return parallelLimit
    }
}
