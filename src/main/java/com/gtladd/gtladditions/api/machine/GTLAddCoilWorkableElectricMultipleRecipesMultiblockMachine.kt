package com.gtladd.gtladditions.api.machine

import org.gtlcore.gtlcore.api.recipe.RecipeResult

import com.gregtechceu.gtceu.api.block.ICoilType
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.common.block.CoilBlock

import com.gtladd.gtladditions.api.machine.logic.GTLAddMultipleRecipesLogic
import com.gtladd.gtladditions.utils.MathUtil.minToInt
import com.gtladd.gtladditions.utils.MathUtil.pow

import java.util.function.BiPredicate
import kotlin.math.max

open class GTLAddCoilWorkableElectricMultipleRecipesMultiblockMachine(holder: IMachineBlockEntity) :
    GTLAddWorkableElectricMultipleRecipesMachine(holder) {
    var coilType: ICoilType = CoilBlock.CoilType.CUPRONICKEL

    companion object {
        private val EBF_CHECK: BiPredicate<GTRecipe, IRecipeLogicMachine> =
            BiPredicate { recipe, machine ->
                (machine as GTLAddCoilWorkableElectricMultipleRecipesMultiblockMachine).let {
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
        (multiblockState.matchContext.get("CoilType") as ICoilType).let { this.coilType = it }
    }

    fun getCoilTier(): Int = coilType.tier

    class CoilMachineLogic(machine: GTLAddCoilWorkableElectricMultipleRecipesMultiblockMachine) :
        GTLAddMultipleRecipesLogic(machine) {

        override fun checkRecipe(recipe: GTRecipe): Boolean = super.checkRecipe(recipe) && EBF_CHECK.test(recipe, machine)
    }
}
