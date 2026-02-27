package com.gtladd.gtladditions.common.machine.muiltblock.controller

import org.gtlcore.gtlcore.api.recipe.IGTRecipe

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.recipe.GTRecipe

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

import com.gtladd.gtladditions.api.machine.GTLAddWorkableElectricMultipleRecipesMachine
import com.gtladd.gtladditions.api.machine.GTLAddWorkableElectricParallelHatchMultipleRecipesMachine
import com.gtladd.gtladditions.api.machine.logic.GTLAddMultipleRecipesLogic
import com.gtladd.gtladditions.api.recipe.FastRecipeModify
import com.gtladd.gtladditions.common.machine.muiltblock.controller.Resource.CreateUltimateBattery
import com.gtladd.gtladditions.common.machine.muiltblock.controller.Resource.CryotheumDust
import com.gtladd.gtladditions.utils.MachineUtil.inputItemStack
import com.gtladd.gtladditions.utils.MathUtil.ln
import com.gtladd.gtladditions.utils.MathUtil.maxToInt
import com.gtladd.gtladditions.utils.MathUtil.pow
import com.gtladd.gtladditions.utils.MathUtil.safeToInt

class AntientropyCondensationCenter(holder: IMachineBlockEntity) : GTLAddWorkableElectricParallelHatchMultipleRecipesMachine(holder) {
    @Persisted
    private var isModify = false

    override fun createRecipeLogic(vararg args: Any) = AntientropyCondensationRecipeLogic(this)

    override fun modifyRecipe(recipe: GTRecipe) = if (isModify) FastRecipeModify.ReduceResult(.5, .7) else super.modifyRecipe(recipe)

    override fun onUse(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult {
        if (!world.isClientSide && !this.isModify) {
            val stack = player.`kjs$getMainHandItem`()
            if (stack.`is`(CreateUltimateBattery)) {
                val a = stack.count - 1
                player.`kjs$setMainHandItem`(if (a == 0) ItemStack.EMPTY else ItemStack(CreateUltimateBattery, a))
                this.isModify = true
            }
        }
        return InteractionResult.PASS
    }

    override fun getFieldHolder() = MANAGED_FIELD_HOLDER

    companion object {
        val MANAGED_FIELD_HOLDER = ManagedFieldHolder(AntientropyCondensationCenter::class.java, GTLAddWorkableElectricMultipleRecipesMachine.MANAGED_FIELD_HOLDER)
    }

    class AntientropyCondensationRecipeLogic(machine: AntientropyCondensationCenter) : GTLAddMultipleRecipesLogic(machine) {
        override fun testBefore(obj: Object): Boolean = (this.machine as AntientropyCondensationCenter).let {
            val l = (obj as? IGTRecipe)?.realParallels ?: (obj as Long)
            val count = 5 * (l / 2.pow(19) + 51.ln(l)) / 1.maxToInt(it.tier - 9)
            return it.inputItemStack(ItemStack(CryotheumDust, count.safeToInt))
        }
    }
}
