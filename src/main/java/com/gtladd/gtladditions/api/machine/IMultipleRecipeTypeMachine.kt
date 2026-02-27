package com.gtladd.gtladditions.api.machine

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine
import com.gregtechceu.gtceu.api.recipe.GTRecipeType

interface IMultipleRecipeTypeMachine : IRecipeLogicMachine {

    val multiRecipeTypes: Array<GTRecipeType>

    val multiRecipeType: GTRecipeType get() = this.multiRecipeTypes[activeRecipeType]
}
