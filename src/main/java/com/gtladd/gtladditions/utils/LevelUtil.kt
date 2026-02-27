package com.gtladd.gtladditions.utils

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

import kotlin.math.floor

object LevelUtil {
    fun throwItemEntity(entity: ItemEntity, itemStack: ItemStack) {
        val level = entity.level()
        val random: RandomSource = level.getRandom()
        val x = floor(entity.x) + .25 + random.nextDouble() * .5
        val y = floor(entity.y) + .25 + random.nextDouble() * .5
        val z = floor(entity.z) + .25 + random.nextDouble() * .5
        val xSpeed = random.nextDouble() * .25 - 0.125
        val ySpeed = random.nextDouble() * .25 - 0.125
        val zSpeed = random.nextDouble() * .25 - 0.125
        val newEntity = ItemEntity(level, x, y, z, itemStack)
        newEntity.setDeltaMovement(xSpeed, ySpeed, zSpeed)
        level.addFreshEntity(newEntity)
    }
    fun Level.throwItemEntity(pos: BlockPos, itemStack: ItemStack) {
        val random: RandomSource = this.getRandom()
        val x = floor(pos.x.toDouble()) + .25 + random.nextDouble() * .5
        val y = floor(pos.y.toDouble()) + .25 + random.nextDouble() * .5
        val z = floor(pos.z.toDouble()) + .25 + random.nextDouble() * .5
        val xSpeed = random.nextDouble() * .25 - 0.125
        val ySpeed = random.nextDouble() * .25 - 0.125
        val zSpeed = random.nextDouble() * .25 - 0.125
        val newEntity = ItemEntity(this, x, y, z, itemStack)
        newEntity.setDeltaMovement(xSpeed, ySpeed, zSpeed)
        this.addFreshEntity(newEntity)
    }
}
