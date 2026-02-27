package com.gtladd.gtladditions.api.machine

import org.gtlcore.gtlcore.api.recipe.RecipeResult

import com.gregtechceu.gtceu.api.block.ICoilType
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.common.block.CoilBlock

import com.gtladd.gtladditions.api.machine.logic.GTLAddMultiRecipesTypesLogic
import com.gtladd.gtladditions.utils.MathUtil.minToInt
import com.gtladd.gtladditions.utils.MathUtil.pow

import java.util.function.BiPredicate
import kotlin.math.max

open class GTLAddCoilWorkableElectricMultipleRecipesTypesMultiblockMachine(holder: IMachineBlockEntity) :
    GTLAddWorkableElectricMultipleRecipesTypesMachine(holder) {
    var coilType: ICoilType = CoilBlock.CoilType.CUPRONICKEL

    companion object {
        private val EBF_CHECK = BiPredicate { recipe: GTRecipe, machine: IRecipeLogicMachine ->
            (machine as GTLAddCoilWorkableElectricMultipleRecipesTypesMultiblockMachine).let {
                val temp = it.coilType.coilTemperature + 100L * max(0, it.getTier() - 2)
                if (temp < recipe.data.getInt("ebf_temp")) {
                    RecipeResult.of(it, RecipeResult.FAIL_NO_ENOUGH_TEMPERATURE)
                    return@BiPredicate false
                }
                return@BiPredicate true
            }
        }
    }

    override fun createRecipeLogic(vararg args: Any) = CoilMachineLogic(this)

    override fun getRecipeLogic() = super.getRecipeLogic() as CoilMachineLogic

    override fun getMaxParallel(): Int = Int.MAX_VALUE minToInt 2.pow(this.coilType.coilTemperature / 900)

    override fun onStructureFormed() {
        super.onStructureFormed()
        this.coilType = multiblockState.matchContext.get("CoilType")
    }

    class CoilMachineLogic(machine: GTLAddCoilWorkableElectricMultipleRecipesTypesMultiblockMachine) :
        GTLAddMultiRecipesTypesLogic(machine) {

        override fun checkRecipe(recipe: GTRecipe): Boolean = super.checkRecipe(recipe) && EBF_CHECK.test(recipe, machine)
    }
}
