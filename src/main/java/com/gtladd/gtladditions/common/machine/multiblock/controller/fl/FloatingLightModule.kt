package com.gtladd.gtladditions.common.machine.multiblock.controller.fl

import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineModule

import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.api.recipe.GTRecipe

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder

import net.minecraft.core.BlockPos

import com.gtladd.gtladditions.api.machine.GTLAddWorkableElectricMultipleRecipesMachine
import com.gtladd.gtladditions.api.machine.GTLAddWorkableElectricMultipleRecipesTypesMachine
import com.gtladd.gtladditions.api.recipe.FastRecipeModify.ReduceResult
import com.gtladd.gtladditions.utils.FloatingLightPosHelper.calculatePossibleHostPositions

import kotlin.math.pow

abstract class FloatingLightModule(holder: IMachineBlockEntity) :
    GTLAddWorkableElectricMultipleRecipesTypesMachine(holder),
    IModularMachineModule<FloatingLightController, FloatingLightModule>,
    IMachineLife {

    companion object {
        val MANAGED_FIELD_HOLDER = ManagedFieldHolder(FloatingLightModule::class.java, GTLAddWorkableElectricMultipleRecipesMachine.MANAGED_FIELD_HOLDER)
    }

    override fun getFieldHolder() = MANAGED_FIELD_HOLDER

    @Persisted
    private var hostPosition: BlockPos? = null
    private var host: FloatingLightController? = null

    override fun modifyRecipe(recipe: GTRecipe): ReduceResult {
        val modify = host!!.tier - GTValues.UIV
        return if (modify > 0) {
            ReduceResult(0.9.pow(modify), 0.9.pow(modify))
        } else {
            super.modifyRecipe(recipe)
        }
    }

    override fun getThread() = 128 * if (host?.isDouble == true) 2 else 1

    override fun testBefore(obj: Object): Boolean {
        val circuit = host?.getCircuit()
        return (host?.tier ?: 0) >= GTValues.UIV && (circuit == 1 || circuit == 2)
    }

    override fun onMachineRemoved() = removeFromHost(this.host)

    override fun getHostPosition() = this.hostPosition

    override fun setHostPosition(pos1: BlockPos?) {
        this.hostPosition = pos1
    }

    override fun getHost() = this.host

    override fun setHost(flController: FloatingLightController?) {
        this.host = flController
    }

    override fun getHostType() = FloatingLightController::class.java

    override fun getHostScanPositions(): Array<BlockPos> = calculatePossibleHostPositions(pos)
}
