package com.gtladd.gtladditions.common.saved

import org.gtlcore.gtlcore.common.machine.multiblock.electric.HarmonyMachine

import com.gregtechceu.gtceu.api.machine.MetaMachine

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

import com.gtladd.gtladditions.common.machine.multiblock.controller.ArcanicAstrograph
import it.unimi.dsi.fastutil.ints.IntIntPair
import it.unimi.dsi.fastutil.longs.LongArraySet

class HarmonySaved {

    companion object {
        var INSTANCE = HarmonySaved()
    }

    private val data: LongArraySet = LongArraySet()

    fun update(saved: Long) {
        data.add(saved)
    }

    fun remove(saved: Long) {
        data.remove(saved)
    }

    fun getMachineCount(level: Level): IntIntPair {
        var m1 = 0
        var m2 = 0
        data.map { BlockPos.of(it) }.mapNotNull { MetaMachine.getMachine(level, it) }.forEach {
            if (it is HarmonyMachine && it.recipeLogic.isWorking) {
                if (it is ArcanicAstrograph) {
                    m2++
                } else {
                    m1++
                }
            }
        }
        return IntIntPair.of(m1, m2)
    }
}
