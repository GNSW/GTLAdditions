package com.gtladd.gtladditions.common.machine.multiblock.controller

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity

import com.gtladd.gtladditions.api.machine.GTLAddCoilWorkableElectricMultipleRecipesTypesMultiblockMachine
import com.gtladd.gtladditions.utils.MathUtil.minToInt
import com.gtladd.gtladditions.utils.MathUtil.pow

open class BiosphereIII(holder: IMachineBlockEntity) : GTLAddCoilWorkableElectricMultipleRecipesTypesMultiblockMachine(holder) {
    override fun getMaxParallel() = Int.MAX_VALUE minToInt 2.pow(this.coilType.coilTemperature / 1200)
}
