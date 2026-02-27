package com.gtladd.gtladditions.common.machine

import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferProxyPartMachine

import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility
import com.gregtechceu.gtceu.api.pattern.MultiblockState
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate
import com.gregtechceu.gtceu.api.pattern.predicates.PredicateBlocks

import com.gtladd.gtladditions.common.machine.muiltblock.controller.DraconicCollapseCore

object GTLAddPredicates {

    fun dccBlocks(): TraceabilityPredicate {
        return TraceabilityPredicate(object : PredicateBlocks(*PartAbility.EXPORT_ITEMS.allBlocks.toTypedArray()) {
            override fun test(blockWorldState: MultiblockState): Boolean {
                if (super.test(blockWorldState)) {
                    val machine = MetaMachine.getMachine(blockWorldState.getWorld(), blockWorldState.pos)
                    if (machine is MEPatternBufferPartMachine || machine is MEPatternBufferProxyPartMachine) {
                        return (blockWorldState.controller as? DraconicCollapseCore)?.isSuper == true
                    }
                    return true
                }
                return false
            }
        })
    }
}
