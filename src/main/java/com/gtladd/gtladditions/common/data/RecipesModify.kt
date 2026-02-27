package com.gtladd.gtladditions.common.data

import org.gtlcore.gtlcore.common.data.GTLMaterials
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes.*

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability.CAP
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.GAS
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.LIQUID
import com.gregtechceu.gtceu.api.recipe.content.Content
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient
import com.gregtechceu.gtceu.common.data.GTMaterials.Helium
import com.gregtechceu.gtceu.common.data.GTRecipeTypes.VACUUM_RECIPES
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder

import com.lowdragmc.lowdraglib.utils.LocalizationUtils

import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluid

import com.gtladd.gtladditions.api.recipe.ContentList
import com.gtladd.gtladditions.common.machine.muiltblock.controller.Resource.cellSet
import com.gtladd.gtladditions.common.machine.muiltblock.controller.Resource.itemSet
import com.gtladd.gtladditions.common.recipe.GTLAddRecipesTypes
import com.gtladd.gtladditions.utils.GTRecipeUtils.amount
import com.gtladd.gtladditions.utils.GTRecipeUtils.stack
import com.gtladd.gtladditions.utils.MathUtil.safeToInt
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectIntPair
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap

object RecipesModify {
    val RTBERecipeItemMap: Int2ObjectOpenHashMap<Array<ObjectIntPair<Item>>> = Int2ObjectOpenHashMap(64)
    val RTBERecipeFluidMap: Int2ObjectOpenHashMap<Array<ObjectIntPair<Fluid>>> = Int2ObjectOpenHashMap(64)

    @JvmStatic
    fun init() {
        INTEGRATED_ORE_PROCESSOR.addDataInfo {
            if (it.contains("handle")) LocalizationUtils.format("gtceu.integrated_ore_processor.advanced")
            ""
        }
        val filterLiquid = { content: Content -> CAP.of(content.content)?.test(Helium.getFluid(LIQUID, 1L)) ?: false }
        val filterGas = { content: Content -> CAP.of(content.content)?.test(Helium.getFluid(GAS, 1L)) ?: false }
        VACUUM_RECIPES.onRecipeBuild { recipeBuilder, provider ->
            val builder = GTLAddRecipesTypes.ANTIENTROPY_CONDENSATION.copyFrom(recipeBuilder)
            builder.input[CAP]?.removeIf(filterLiquid)
            builder.output[CAP]?.removeIf(filterGas)
            builder.save(provider)
        }
        PLASMA_CONDENSER_RECIPES.onRecipeBuild { recipeBuilder, provider ->
            val builder = GTLAddRecipesTypes.ANTIENTROPY_CONDENSATION.copyFrom(recipeBuilder)
            builder.input[CAP]?.removeIf(filterLiquid)
            builder.output[CAP]?.removeIf(filterGas)
            builder.save(provider)
        }
        DISTORT_RECIPES.onRecipeBuild { recipeBuilder, provider ->
            val builder = GTLAddRecipesTypes.TIME_SPACE_DISTORTER.copyFrom(recipeBuilder)
            val (i, o) = modify(builder)
            builder.input.replaceAll { t, u -> i[t] }
            builder.output.replaceAll { t, u -> o[t] }
            builder.save(provider)
        }
        QFT_RECIPES.onRecipeBuild { recipeBuilder, provider ->
            val builder = GTLAddRecipesTypes.TIME_SPACE_DISTORTER.copyFrom(recipeBuilder)
            if (builder.id.path.equals("make_miracle_crystal")) {
                builder.save(provider)
                return@onRecipeBuild
            }
            val (i, o) = modify(builder)
            builder.input.replaceAll { t, u -> i[t] }
            builder.output.replaceAll { t, u -> o[t] }
            builder.save(provider)
        }
        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.onRecipeBuild { recipeBuilder, provider ->
            val builder = GTLAddRecipesTypes.RECURSIVE_REVERSE_FORGE.copyFrom(recipeBuilder)
            builder.id(recipeBuilder.id.withSuffix("_1"))
            builder.input.forEach { t, u ->
                if (t == ItemRecipeCapability.CAP) {
                    u.forEach {
                        val item = (it.content as Ingredient).`kjs$getFirst`().item
                        when (item) {
                            in cellSet -> it.slotName = "c"
                            in itemSet -> it.slotName = "i"
                        }
                    }
                }
            }
            builder.output.forEach { t, u ->
                if (t == ItemRecipeCapability.CAP) {
                    val a = ObjectArrayList<ObjectIntPair<Item>>()
                    u.forEach {
                        val item = (it.content as Ingredient).`kjs$getFirst`().item
                        when (item) {
                            in cellSet -> it.slotName = "c"
                            in itemSet -> it.slotName = "i"
                            else -> a.add(ObjectIntPair.of(item, it.amount(t).safeToInt))
                        }
                    }
                    RTBERecipeItemMap.put(builder.id.hashCode(), a.toTypedArray())
                } else if (t == CAP) {
                    val l =
                        u
                            .map { it.content as FluidIngredient }
                            .filter {
                                it.stack.fluid !=
                                    GTLMaterials.DimensionallyTranscendentResidue.fluid
                            }.map { ObjectIntPair.of(it.stack.fluid, it.amount.safeToInt) }
                            .toTypedArray()
                    RTBERecipeFluidMap.put(builder.id.hashCode(), l)
                }
            }
            builder.save(provider)
        }
        STELLAR_FORGE_RECIPES.onRecipeBuild { recipeBuilder, provider ->
            val builder = GTLAddRecipesTypes.RECURSIVE_REVERSE_FORGE.copyFrom(recipeBuilder)
            builder.id(recipeBuilder.id.withSuffix("_2"))
            builder.input.forEach { t, u ->
                if (t == ItemRecipeCapability.CAP) {
                    u.forEach {
                        val item = (it.content as Ingredient).`kjs$getFirst`().item
                        when (item) {
                            in cellSet -> it.slotName = "c"
                            in itemSet -> it.slotName = "i"
                        }
                    }
                }
            }
            builder.output.forEach { t, u ->
                if (t == ItemRecipeCapability.CAP) {
                    val a = ObjectArrayList<ObjectIntPair<Item>>()
                    u.forEach {
                        val item = (it.content as Ingredient).`kjs$getFirst`().item
                        when (item) {
                            in cellSet -> it.slotName = "c"
                            in itemSet -> it.slotName = "i"
                            else -> a.add(ObjectIntPair.of(item, it.amount(t).safeToInt))
                        }
                    }
                    RTBERecipeItemMap.put(builder.id.hashCode(), a.toTypedArray())
                } else if (t == CAP) {
                    val l =
                        u
                            .map { it.content as FluidIngredient }
                            .filter {
                                it.stack.fluid !=
                                    GTLMaterials.DimensionallyTranscendentResidue.fluid
                            }.map { ObjectIntPair.of(it.stack.fluid, it.amount.safeToInt) }
                            .toTypedArray()
                    RTBERecipeFluidMap.put(builder.id.hashCode(), l)
                }
            }
            builder.save(provider)
        }
    }

    private fun modify(builder: GTRecipeBuilder,): Pair<Map<RecipeCapability<*>, MutableList<Content>>, Map<RecipeCapability<*>, MutableList<Content>>> {
        val ci = Reference2ObjectOpenHashMap<RecipeCapability<*>, MutableList<Content>>(builder.input.size)
        builder.input.entries.forEach { (c, l) ->
            if (!l.isEmpty()) {
                val cl = ContentList(l.size)
                for (t in l) {
                    if (t.chance >= t.maxChance) {
                        cl.addMaxChanceContent(t.content)
                    } else {
                        if (c == ItemRecipeCapability.CAP) {
                            val item = (t.content as Ingredient).`kjs$getFirst`()
                            if (item.`kjs$getId`().contains("nanoswarm")) {
                                cl.addNoCustomContent(t.content)
                            } else {
                                cl.addChanceItemStack(item, t.chance, t.maxChance * 10)
                            }
                        } else {
                            cl.addNoCustomContent(t.content)
                        }
                    }
                }
                ci.put(c, cl)
            }
        }
        val co = Reference2ObjectOpenHashMap<RecipeCapability<*>, MutableList<Content>>(builder.output.size)
        builder.output.entries.forEach { (c, l) ->
            if (!l.isEmpty()) {
                val cl = ContentList(l.size)
                l.forEach { cl.addMaxChanceContent(it.content) }
                co.put(c, cl)
            }
        }
        return Pair(ci, co)
    }
}
