package com.gtladd.gtladditions.data.recipes.newmachinerecipe

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper
import com.gregtechceu.gtceu.api.data.tag.TagPrefix
import com.gregtechceu.gtceu.common.data.GTMaterials

import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks.*

import com.gtladd.gtladditions.GTLAdditions.id
import com.gtladd.gtladditions.api.machine.ConversationMachine.Companion.MagmatterBlock
import com.gtladd.gtladditions.api.machine.ConversationMachine.Companion.MagmatterIngot
import com.gtladd.gtladditions.api.machine.ConversationMachine.Companion.MagnetohydrodynamicallyConstrainedStarMatterBlock
import com.gtladd.gtladditions.common.machine.multiblock.controller.CreateAggregationMachine.Companion.ChainCommandBlockBroken
import com.gtladd.gtladditions.common.machine.multiblock.controller.CreateAggregationMachine.Companion.ChainCommandBlockCore
import com.gtladd.gtladditions.common.machine.multiblock.controller.CreateAggregationMachine.Companion.CommandBlockBroken
import com.gtladd.gtladditions.common.machine.multiblock.controller.CreateAggregationMachine.Companion.RepeatingCommandBlockCore
import com.gtladd.gtladditions.common.recipe.GTLAddRecipesTypes.TRANSMUTATION_BLOCK_CONVERSION
import com.gtladd.gtladditions.utils.Registries.getBlock

import java.util.function.Consumer

object EMResonanceConversionField {
    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        addRecipe(BONE_BLOCK, "kubejs:essence_block".getBlock, provider)
        addRecipe(OAK_LOG, CRIMSON_STEM, provider)
        addRecipe(BIRCH_LOG, WARPED_STEM, provider)
        addRecipe(ChemicalHelper.getBlock(TagPrefix.block, GTMaterials.Calcium), BONE_BLOCK, provider)
        addRecipe(MOSS_BLOCK, SCULK, provider)
        addRecipe(GRASS_BLOCK, MOSS_BLOCK, provider)
        addRecipe("kubejs:infused_obsidian".getBlock, "kubejs:draconium_block_charged".getBlock, provider)

        addRecipe(MagnetohydrodynamicallyConstrainedStarMatterBlock, COMMAND_BLOCK, provider)
        TRANSMUTATION_BLOCK_CONVERSION.recipeBuilder(id(MagmatterBlock.descriptionId))
            .inputItems(MagmatterIngot, 64).outputItems(MagmatterBlock).save(provider)

        TRANSMUTATION_BLOCK_CONVERSION.recipeBuilder(id(CHAIN_COMMAND_BLOCK.descriptionId))
            .inputItems(CommandBlockBroken.asItem()).inputItems(ChainCommandBlockCore.item)
            .outputItems(CHAIN_COMMAND_BLOCK.asItem()).save(provider)
        TRANSMUTATION_BLOCK_CONVERSION.recipeBuilder(id(REPEATING_COMMAND_BLOCK.descriptionId))
            .inputItems(ChainCommandBlockBroken.asItem()).inputItems(RepeatingCommandBlockCore.item)
            .outputItems(REPEATING_COMMAND_BLOCK.asItem()).save(provider)
    }

    private fun addRecipe(input: Block, output: Block, provider: Consumer<FinishedRecipe>) {
        TRANSMUTATION_BLOCK_CONVERSION.recipeBuilder(id(output.descriptionId))
            .inputItems(input.asItem()).outputItems(output.asItem()).save(provider)
    }
}
