package com.gtladd.gtladditions.utils

import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine
import org.gtlcore.gtlcore.api.recipe.RecipeResult.fail
import org.gtlcore.gtlcore.api.recipe.RecipeResult.of
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper.handleRecipeInputNocache
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper.matchRecipeInputNocache

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour.getCircuitConfiguration
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder

import com.lowdragmc.lowdraglib.side.fluid.FluidStack

import net.minecraft.network.chat.Component.translatable
import net.minecraft.world.item.ItemStack

import com.gtladd.gtladditions.api.recipe.ContentList
import com.hepdd.gtmthings.common.block.machine.multiblock.part.HugeBusPartMachine

object MachineUtil {

    fun WorkableMultiblockMachine.inputItemStack(stack: ItemStack): Boolean {
        val builder = GTRecipeBuilder.ofRaw()
        builder.input.put(ItemRecipeCapability.CAP, ContentList(1).addMaxChanceContent(stack))
        val recipe = builder.buildRawRecipe()
        if (matchRecipeInputNocache(this, recipe)) {
            return handleRecipeInputNocache(this, recipe)
        } else {
            of(this, fail(translatable("gtceu.recipe.fail.no.input.item", stack.displayName.string + "x${stack.count}")))
            return false
        }
    }

    fun WorkableMultiblockMachine.inputFluidStack(stack: FluidStack): Boolean {
        val builder = GTRecipeBuilder.ofRaw()
        builder.input.put(FluidRecipeCapability.CAP, ContentList(1).addMaxChanceContent(stack))
        val recipe = builder.buildRawRecipe()
        if (matchRecipeInputNocache(this, recipe)) {
            return handleRecipeInputNocache(this, recipe)
        } else {
            of(this, fail(translatable("gtceu.recipe.fail.no.input.fluid", stack.displayName.string + "x${stack.amount}mB")))
            return false
        }
    }

    fun MultiblockControllerMachine.getCircuit(): Int {
        for (p in this.parts) {
            if (p is ItemBusPartMachine) {
                val i = p.circuitInventory.getStackInSlot(0)
                if (i.isEmpty) continue
                return getCircuitConfiguration(i)
            } else if (p is HugeBusPartMachine) {
                val i = p.circuitInventory.getStackInSlot(0)
                if (i.isEmpty) continue
                return getCircuitConfiguration(i)
            }
        }
        return 0
    }

    fun WorkableMultiblockMachine.maintenance(): Double = (this as? IRecipeCapabilityMachine)?.maintenanceMachine?.durationMultiplier?.toDouble() ?: 1.0
}
