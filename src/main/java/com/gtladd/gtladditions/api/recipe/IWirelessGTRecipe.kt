package com.gtladd.gtladditions.api.recipe

import com.gregtechceu.gtceu.api.capability.recipe.IO
import com.gregtechceu.gtceu.api.recipe.GTRecipe

interface IWirelessGTRecipe {
    companion object {
        fun of(recipe: GTRecipe): IWirelessGTRecipe {
            return recipe as IWirelessGTRecipe
        }
    }

    var iO: IO
        get() = IO.NONE
        set(io) {
        }

    var wirelessEUt: Double
        get() = 0.0
        set(wirelessEUt) {
        }
}
