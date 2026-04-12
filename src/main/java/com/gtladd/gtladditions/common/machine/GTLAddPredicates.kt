package com.gtladd.gtladditions.common.machine

import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferProxyPartMachine

import com.gregtechceu.gtceu.api.GTCEuAPI
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility
import com.gregtechceu.gtceu.api.pattern.MultiblockState
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError
import com.gregtechceu.gtceu.api.pattern.predicates.PredicateBlocks
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate
import com.gregtechceu.gtceu.common.block.CoilBlock

import com.lowdragmc.lowdraglib.utils.BlockInfo

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.state.properties.SlabType

import com.gtladd.gtladditions.common.machine.multiblock.controller.DraconicCollapseCore
import com.gtladd.gtladditions.utils.ComponentUtil.toComponent

import java.util.function.Predicate

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

    fun slabBlock(slabType: SlabType, vararg block: Block): TraceabilityPredicate {
        return TraceabilityPredicate(object : PredicateBlocks(*block) {
            override fun test(blockWorldState: MultiblockState): Boolean {
                return blockWorldState.blockState.getValue(SlabBlock.TYPE) == slabType
            }
        })
    }

    fun heatingCoils(temperature: Int): TraceabilityPredicate {
        return TraceabilityPredicate(object : SimplePredicate(
            Predicate {
                for (entry in GTCEuAPI.HEATING_COILS.entries) {
                    if (it.blockState.`is`(entry.value.get())) {
                        val stats = entry.key
                        val currentCoil = it.matchContext.getOrPut("CoilType", stats)
                        if (currentCoil != stats) {
                            it.setError(PatternStringError("gtceu.multiblock.pattern.error.coils"))
                            return@Predicate false
                        }
                        return@Predicate true
                    }
                }
                false
            },
            {
                val (matched, notMatched) = GTCEuAPI.HEATING_COILS.entries.partition { it.key.coilTemperature >= temperature }
                (matched.sortedBy { it.key.coilTemperature } + notMatched.sortedBy { it.key.coilTemperature }).map { BlockInfo.fromBlockState(it.value.get().defaultBlockState()) }.toTypedArray()
            }
        ) {
            override fun test(blockWorldState: MultiblockState): Boolean {
                if (super.test(blockWorldState)) return (blockWorldState.blockState.block as CoilBlock).coilType.coilTemperature >= temperature
                return false
            }
        }).addTooltips("gtceu.multiblock.pattern.error.coils".toComponent)
    }
}
