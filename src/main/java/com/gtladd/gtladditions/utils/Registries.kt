package com.gtladd.gtladditions.utils

import org.gtlcore.gtlcore.utils.Registries.*

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

object Registries {
    val String.getItem: Item get() = getItem(this)
    fun String.getItemStack(int: Int = 1): ItemStack = getItemStack(this, int)
    val String.getBlock: Block get() = getBlock(this)
    val String.getFluid: Fluid get() = getFluid(this)
}
