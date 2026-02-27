package com.gtladd.gtladditions.utils

import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids

import appeng.api.stacks.AEFluidKey
import appeng.api.stacks.AEItemKey
import appeng.api.stacks.GenericStack

object AEUtil {
    val GenericStack.getItem: Item get() = (this.what as? AEItemKey)?.item ?: Items.AIR
    val GenericStack.getFluid: Fluid get() = (this.what as? AEFluidKey)?.fluid ?: Fluids.WATER
}
