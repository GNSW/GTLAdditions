package com.gtladd.gtladditions.common.machine.multiblock.controller.fl

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

import kotlin.collections.iterator

/**代码参考自GTLAdditions分支
 * &#064;line <a href="https://github.com/Dragonators/GTLAdditions">...</a>
 */
object FloatingLightPosHelper {
    private val BASE_OFFSETS = arrayOf(
        intArrayOf(28, -8, -14),
        intArrayOf(34, -8, -14),
        intArrayOf(40, -8, -14),
        intArrayOf(46, -8, -14),
        intArrayOf(52, -8, -14),
        intArrayOf(58, -8, -14),
        intArrayOf(64, -8, -14),
        intArrayOf(70, -8, -14),
        intArrayOf(28, -8, 14),
        intArrayOf(34, -8, 14),
        intArrayOf(40, -8, 14),
        intArrayOf(46, -8, 14),
        intArrayOf(52, -8, 14),
        intArrayOf(58, -8, 14),
        intArrayOf(64, -8, 14),
        intArrayOf(70, -8, 14),
        intArrayOf(28, -12, -14),
        intArrayOf(34, -12, -14),
        intArrayOf(40, -12, -14),
        intArrayOf(46, -12, -14),
        intArrayOf(52, -12, -14),
        intArrayOf(58, -12, -14),
        intArrayOf(64, -12, -14),
        intArrayOf(70, -12, -14),
        intArrayOf(28, -12, 14),
        intArrayOf(34, -12, 14),
        intArrayOf(40, -12, 14),
        intArrayOf(46, -12, 14),
        intArrayOf(52, -12, 14),
        intArrayOf(58, -12, 14),
        intArrayOf(64, -12, 14),
        intArrayOf(70, -12, 14)
    )

    private val offsetsByDirection by lazy {
        mapOf(
            Direction.EAST to BASE_OFFSETS,
            Direction.SOUTH to rotateOffsets(BASE_OFFSETS, 1),
            Direction.WEST to rotateOffsets(BASE_OFFSETS, 2),
            Direction.NORTH to rotateOffsets(BASE_OFFSETS, 3)
        )
    }

    fun calculateModulePositions(hostPos: BlockPos, hostFacing: Direction): Array<BlockPos> {
        val offsets = offsetsByDirection.getValue(hostFacing)
        return Array(offsets.size) { i ->
            val offset = offsets[i]
            hostPos.offset(offset[0], offset[1], offset[2])
        }
    }

    fun calculatePossibleHostPositions(modulePos: BlockPos): Array<BlockPos> {
        val result = mutableListOf<BlockPos>()

        for ((_, offsets) in offsetsByDirection) {
            for (offset in offsets) {
                val hostPos = modulePos.offset(-offset[0], -offset[1], -offset[2])
                result.add(hostPos)
            }
        }

        return result.toTypedArray()
    }

    @Suppress("SameParameterValue")
    private fun rotateOffsets(offsets: Array<IntArray>, rotation: Int): Array<IntArray> {
        return Array(offsets.size) { i ->
            val (x, y, z) = offsets[i]
            when (rotation % 4) {
                0 -> intArrayOf(x, y, z)
                1 -> intArrayOf(-z, y, x)
                2 -> intArrayOf(-x, y, -z)
                3 -> intArrayOf(z, y, -x)
                else -> intArrayOf(x, y, z)
            }
        }
    }
}
