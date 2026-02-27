package com.gtladd.gtladditions.api.machine

import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity

open class GTLAddWorkableElectricParallelHatchMultipleRecipesMachine(holder: IMachineBlockEntity) :
    GTLAddWorkableElectricMultipleRecipesMachine(holder) {
    override fun getMaxParallel(): Int = GTLRecipeModifiers.getHatchParallel(this)
}
