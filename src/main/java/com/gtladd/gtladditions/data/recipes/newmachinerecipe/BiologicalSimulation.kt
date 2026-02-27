package com.gtladd.gtladditions.data.recipes.newmachinerecipe

import org.gtlcore.gtlcore.common.data.GTLMaterials.BiohmediumSterilized
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes.INCUBATOR_RECIPES

import com.gregtechceu.gtceu.api.GTValues.UV
import com.gregtechceu.gtceu.api.GTValues.VA
import com.gregtechceu.gtceu.common.data.GTMaterials.Biomass
import com.gregtechceu.gtceu.common.data.GTMaterials.Milk
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder

import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items.*

import com.gtladd.gtladditions.GTLAdditions.id
import com.gtladd.gtladditions.common.recipe.GTLAddRecipesTypes
import com.gtladd.gtladditions.utils.MathUtil.minToInt
import com.gtladd.gtladditions.utils.Registries.getItem
import com.gtladd.gtladditions.utils.Registries.getItemStack

import java.util.function.Consumer

object BiologicalSimulation {
    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        val swords = arrayOf(
            Sword(DIAMOND_SWORD, 15, 1),
            Sword(NETHERITE_SWORD, 5, 5),
            Sword(INF_SWORD, 0, 20)
        )
        val bioData = arrayOf(
            BioData("blaze", netherData, arrayOf(ItemData(BLAZE_ROD, 500)), 7),
            BioData("chicken", overWorldData, arrayOf(ItemData(CHICKEN, 7500), ItemData(FEATHER, 4000), ItemData(EGG, 1000)), 2),
            BioData("cow", overWorldData, arrayOf(ItemData(BEEF, 7500), ItemData(LEATHER, 2500)), 2),
            BioData("drowned", overWorldData, arrayOf(ItemData(ROTTEN_FLESH, 7500), ItemData(COPPER_INGOT, 600)), 3),
            BioData("enderman", endData, arrayOf(ItemData(ENDER_PEARL, 500)), 6),
            BioData("ghast", netherData, arrayOf(ItemData(GUNPOWDER, 6000), ItemData(GHAST_TEAR, 600)), 7),
            BioData("creeper", overWorldData, arrayOf(ItemData(GUNPOWDER, 8000)), 2),
            BioData("zombie", overWorldData, arrayOf(ItemData(ROTTEN_FLESH, 7500), ItemData(IRON_INGOT, 600), ItemData(CARROT, 1500), ItemData(POTATO, 1500)), 2),
            BioData("zombie_villager", overWorldData, arrayOf(ItemData(ROTTEN_FLESH, 7500), ItemData(IRON_INGOT, 600), ItemData(CARROT, 1500), ItemData(POTATO, 1500)), 2),
            BioData("husk", overWorldData, arrayOf(ItemData(ROTTEN_FLESH, 7500), ItemData(IRON_INGOT, 600), ItemData(CARROT, 1500), ItemData(POTATO, 1500)), 2),
            BioData("zombified_piglin", netherData, arrayOf(ItemData(ROTTEN_FLESH, 7500), ItemData(GOLD_INGOT, 600), ItemData(GOLD_NUGGET, 1000)), 3),
            BioData("pig", overWorldData, arrayOf(ItemData(PORKCHOP, 8000)), 2),
            BioData("sheep", overWorldData, arrayOf(ItemData(MUTTON, 8000), ItemData(WHITE_WOOL, 5000)), 2),
            BioData("skeleton", overWorldData, arrayOf(ItemData(BONE, 7500), ItemData(ARROW, 6500)), 2),
            BioData("slime", overWorldData, arrayOf(ItemData(SLIME_BALL, 5000)), 3),
            BioData("spider", overWorldData, arrayOf(ItemData(STRING, 7000), ItemData(SPIDER_EYE, 2000)), 2),
            BioData("vindicator", overWorldData, arrayOf(ItemData(EMERALD, 1000)), 3),
            BioData("witch", overWorldData, arrayOf(ItemData(REDSTONE, 600), ItemData(GLOWSTONE_DUST, 600), ItemData(SUGAR, 3500), ItemData(GLASS_BOTTLE, 3500), ItemData(STICK, 5000), ItemData(GUNPOWDER, 3500), ItemData(SPIDER_EYE, 600)), 3),
            BioData("wither_skeleton", netherData, arrayOf(ItemData(BONE, 7500), ItemData(COAL, 6500), ItemData(WITHER_SKELETON_SKULL, 500)), 8),
            BioData("rabbit", overWorldData, arrayOf(ItemData(RABBIT, 7000), ItemData(RABBIT_HIDE, 1000), ItemData(RABBIT_FOOT, 500)), 3),
            BioData("donkey", overWorldData, arrayOf(ItemData(LEATHER, 5000)), 2),
            BioData("llama", overWorldData, arrayOf(ItemData(LEATHER, 5000)), 2),
            BioData("cat", overWorldData, arrayOf(ItemData(STRING, 5000)), 2),
            BioData("panda", overWorldData, arrayOf(ItemData(BAMBOO, 5000)), 3),
            BioData("polar_bear", overWorldData, arrayOf(ItemData(COD, 5000), ItemData(SALMON, 5000)), 3),
            BioData("elder_guardian", overWorldData, arrayOf(ItemData(PRISMARINE_SHARD, 7500), ItemData(PRISMARINE_CRYSTALS, 3300), ItemData(TROPICAL_FISH, 5000), ItemData(PUFFERFISH, 5000)), 6)
        )
        for (b in bioData) {
            for (s in swords) generateRecipe(b, s, provider)
            setSpawnEggRecipes(b, provider)
        }
        generateSpecialRecipes(provider)
    }

    private fun generateRecipe(bioData: BioData, sword: Sword, provider: Consumer<FinishedRecipe>) {
        val builder = GTLAddRecipesTypes.BIOLOGICAL_SIMULATION.recipeBuilder(
            id(bioData.name + (if (sword.damage > 10) "_1" else (if (sword.damage > 0) "_2" else "_3")))
        )
            .notConsumable(("minecraft:" + bioData.name + "_spawn_egg").getItem).notConsumable(bioData.data)
        if (sword.sword == INF_SWORD) {
            builder.notConsumable(sword.sword).addData("infinity", true)
        } else {
            builder.chancedInput(sword.sword.defaultInstance, sword.damage, 0)
        }
        builder.inputFluids(Biomass.getFluid((1000 / sword.factor).toLong()))
        addOutputItems(builder, bioData, sword)
        builder.EUt(VA[bioData.eu].toLong()).duration(400 / sword.factor).save(provider)
    }

    private fun setSpawnEggRecipes(bioData: BioData, provider: Consumer<FinishedRecipe>) {
        if (bioData.name == "cow") return
        val builder = INCUBATOR_RECIPES.recipeBuilder(id(bioData.name + "_spawn_egg"))
            .inputItems(BONE, 4)
            .inputFluids(Biomass.getFluid(1000))
            .inputFluids(Milk.getFluid(1000))
        addInputItems(builder, bioData)
        builder.outputItems(("minecraft:" + bioData.name + "_spawn_egg").getItem)
            .EUt(VA[3].toLong()).duration(1200).save(provider)
    }

    private fun addOutputItems(builder: GTRecipeBuilder, bioData: BioData, sword: Sword) = bioData.itemArray.forEach { (item, chance) ->
        builder.chancedOutput(ItemStack(item, sword.factor * 2), chance, 0)
    }

    private fun addInputItems(builder: GTRecipeBuilder, bioData: BioData) {
        for (i in 0..((bioData.itemArray.size - 1) minToInt 3))
            if (bioData.itemArray[i].item != BONE) builder.inputItems(bioData.itemArray[i].item, 4)
        when (bioData.name) {
            "cat" -> builder.circuitMeta(1)
            "zombie" -> builder.circuitMeta(1)
            "zombie_villager" -> builder.circuitMeta(2)
            "husk" -> builder.circuitMeta(3)
            "donkey" -> builder.circuitMeta(1)
            "llama" -> builder.circuitMeta(2)
            "creeper" -> builder.circuitMeta(1)
            "elder_guardian" -> builder.inputItems(WET_SPONGE, 4)
        }
    }

    private fun generateSpecialRecipes(provider: Consumer<FinishedRecipe>) {
        GTLAddRecipesTypes.BIOLOGICAL_SIMULATION.recipeBuilder(id("nether_star"))
            .notConsumable("gtceu:nether_star_block".getItemStack(64))
            .notConsumable(netherData.copyWithCount(64))
            .notConsumable(INF_SWORD)
            .inputFluids(Biomass.getFluid(50))
            .inputFluids(BiohmediumSterilized.getFluid(50))
            .chancedOutput(NETHER_STAR.defaultInstance, 1500, 0)
            .duration(100).addData("infinity", true)
            .EUt(VA[UV].toLong())
            .save(provider)

        GTLAddRecipesTypes.BIOLOGICAL_SIMULATION.recipeBuilder(id("dragon_egg"))
            .notConsumable(DRAGON_HEAD)
            .notConsumable(endData.copyWithCount(64))
            .notConsumable(INF_SWORD)
            .inputFluids(Biomass.getFluid(50))
            .inputFluids(BiohmediumSterilized.getFluid(50))
            .outputItems(DRAGON_EGG)
            .duration(100).addData("infinity", true)
            .EUt(VA[UV].toLong())
            .save(provider)
    }

    @JvmRecord
    internal data class BioData(val name: String, val data: ItemStack, val itemArray: Array<ItemData>, val eu: Int)

    @JvmRecord
    internal data class ItemData(val item: Item, val chance: Int)

    @JvmRecord
    internal data class Sword(val sword: Item, val damage: Int, val factor: Int)

    private val overWorldData = "kubejs:overworld_data".getItemStack()
    private val netherData = "kubejs:nether_data".getItemStack()
    private val endData = "kubejs:end_data".getItemStack()
    private val INF_SWORD = "avaritia:infinity_sword".getItem
}
