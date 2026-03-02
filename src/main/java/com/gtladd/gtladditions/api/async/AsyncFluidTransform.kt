package com.gtladd.gtladditions.api.async

import com.lowdragmc.lowdraglib.Platform
import com.lowdragmc.lowdraglib.async.AsyncThreadData
import com.lowdragmc.lowdraglib.async.IAsyncLogic

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks

import com.gtladd.gtladditions.utils.LevelUtil.throwItemEntity

class AsyncFluidTransform(val level: ServerLevel, val pos: BlockPos, val itemStack: ItemStack) : IAsyncLogic {
    private var tick = 0

    override fun asyncTick(periodID: Long) {
        Platform.getMinecraftServer()?.takeIf { it.isSingleplayer }?.let {
            val mc = Minecraft.getInstance()
            if (mc.isPaused) return
        }
        tick++
        if (tick % 600 == 0) {
            level.setBlock(this.pos, Blocks.AIR.defaultBlockState(), 3)
            if (!this.itemStack.isEmpty) level.throwItemEntity(this.pos, this.itemStack)
            AsyncThreadData.getOrCreate(level).removeAsyncLogic(this)
        }
    }
}
