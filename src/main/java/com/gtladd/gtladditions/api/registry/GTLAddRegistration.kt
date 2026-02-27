package com.gtladd.gtladditions.api.registry

import com.gregtechceu.gtceu.api.block.MetaMachineBlock
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity
import com.gregtechceu.gtceu.api.item.MetaMachineItem
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate

import com.gtladd.gtladditions.GTLAdditions

import java.util.function.Function

class GTLAddRegistration : GTRegistrate(GTLAdditions.MOD_ID) {

    override fun multiblock(name: String, metaMachine: Function<IMachineBlockEntity, out MultiblockControllerMachine>): GTLAddMultiBlockMachineBuilder {
        return GTLAddMultiBlockMachineBuilder.createMulti(name, metaMachine, ::MetaMachineBlock, ::MetaMachineItem) { type, pos, blockState -> MetaMachineBlockEntity.createBlockEntity(type, pos, blockState) }
    }

    companion object {
        @JvmField
        val REGISTRATE: GTLAddRegistration = GTLAddRegistration()
    }
}
