package com.gtladd.gtladditions.api.machine.logic

import com.gregtechceu.gtceu.api.recipe.lookup.GTRecipeLookup

import com.gtladd.gtladditions.api.machine.GTLAddWorkableElectricMultipleRecipesTypesMachine

open class GTLAddMultiRecipesTypesLogic(private val typesMachine: GTLAddWorkableElectricMultipleRecipesTypesMachine) :
    GTLAddMultipleRecipesLogic(typesMachine) {

    override fun getLookup(): GTRecipeLookup = typesMachine.multiRecipeType.lookup
}
