package com.gtladd.gtladditions.data.recipes.newmachinerecipe

import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix.nanoswarm
import org.gtlcore.gtlcore.common.data.GTLItems.COMPRESSED_PUFFERFISH
import org.gtlcore.gtlcore.common.data.GTLItems.SUPER_GLUE
import org.gtlcore.gtlcore.common.data.GTLMaterials.*

import com.gregtechceu.gtceu.api.GTValues.*
import com.gregtechceu.gtceu.api.data.tag.TagPrefix.block
import com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot
import com.gregtechceu.gtceu.common.data.GTItems
import com.gregtechceu.gtceu.common.data.GTMaterials.Neutronium

import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.item.Items

import com.gtladd.gtladditions.common.recipe.GTLAddRecipesTypes.ONTOKINETIC
import com.gtladd.gtladditions.utils.Registries.getItemStack

import java.util.function.Consumer

object Ontokinetic {
    @JvmStatic
    fun init(provider: Consumer<FinishedRecipe>) {
        ONTOKINETIC.recipeBuilder("fishbig")
            .inputItems(COMPRESSED_PUFFERFISH, 32)
            .inputItems(SUPER_GLUE, 24)
            .inputItems("kubejs:fishbig_fabric".getItemStack(15))
            .inputItems("kubejs:fishbig_frame".getItemStack(3))
            .inputItems("kubejs:fishbig_hair".getItemStack())
            .inputItems("kubejs:fishbig_hade".getItemStack())
            .inputItems("kubejs:fishbig_body".getItemStack())
            .inputItems("kubejs:fishbig_lhand".getItemStack())
            .inputItems("kubejs:fishbig_rhand".getItemStack())
            .inputItems("kubejs:fishbig_lleg".getItemStack())
            .inputItems("kubejs:fishbig_rleg".getItemStack())
            .inputFluids(MiracleAdhesive.getFluid(1000))
            .outputItems("expatternprovider:fishbig".getItemStack())
            .duration(7200).EUt(1024L * VA[MAX].toLong())
            .save(provider)

        ONTOKINETIC.recipeBuilder("ultimate_stew")
            .inputItems(Items.APPLE)
            .inputItems(Items.GOLDEN_APPLE)
            .inputItems(Items.BREAD)
            .inputItems(Items.KELP)
            .inputItems(Items.COCOA_BEANS)
            .inputItems(Items.CAKE)
            .inputItems(Items.GLISTERING_MELON_SLICE)
            .inputItems(Items.CARROT)
            .inputItems(Items.POISONOUS_POTATO)
            .inputItems(Items.CHORUS_FRUIT)
            .inputItems(Items.BEETROOT)
            .inputItems(Items.MUSHROOM_STEW)
            .inputItems(Items.HONEY_BOTTLE)
            .inputItems(Items.SWEET_BERRIES)
            .inputItems(ingot, CosmicNeutronium)
            .outputItems("avaritia:ultimate_stew".getItemStack())
            .duration(3600).EUt(16L * VA[MAX].toLong())
            .save(provider)

        ONTOKINETIC.recipeBuilder("cosmic_meatballs")
            .inputItems(Items.PORKCHOP)
            .inputItems(Items.MUTTON)
            .inputItems(Items.COD)
            .inputItems(Items.SALMON)
            .inputItems(Items.TROPICAL_FISH)
            .inputItems(Items.PUFFERFISH)
            .inputItems(Items.RABBIT)
            .inputItems(Items.CHICKEN)
            .inputItems(Items.EGG)
            .inputItems(Items.SPIDER_EYE)
            .inputItems(Items.ROTTEN_FLESH)
            .inputItems(ingot, CosmicNeutronium)
            .outputItems("avaritia:cosmic_meatballs".getItemStack())
            .duration(3600).EUt(16L * VA[MAX].toLong())
            .save(provider)

        ONTOKINETIC.recipeBuilder("neutron_credit")
            .notConsumable(nanoswarm, Eternity)
            .notConsumable(GTItems.SHAPE_MOLD_CREDIT)
            .inputItems(block, Neutronium, 64)
            .inputFluids(CosmicNeutronium.getFluid(41497))
            .outputItems("gtceu:neutronium_credit".getItemStack())
            .duration(1200).EUt(4L * VA[MAX].toLong())
            .save(provider)
    }
}
