package com.gtladd.gtladditions.api.machine

import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus

import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator
import com.gregtechceu.gtceu.api.recipe.GTRecipeType

import net.minecraft.network.chat.Component

import com.gtladd.gtladditions.api.machine.gui.GTLAddMachineModeFancyConfigurator
import com.gtladd.gtladditions.api.machine.gui.MultiblockDisplayText
import com.gtladd.gtladditions.api.machine.logic.GTLAddMultiRecipesTypesLogic

open class GTLAddWorkableElectricMultipleRecipesTypesMachine(holder: IMachineBlockEntity) :
    GTLAddWorkableElectricMultipleRecipesMachine(holder), IMultipleRecipeTypeMachine {

    override fun createRecipeLogic(vararg args: Any) = GTLAddMultiRecipesTypesLogic(this)

    override fun getRecipeLogic() = super.getRecipeLogic() as GTLAddMultiRecipesTypesLogic

    override fun attachSideTabs(sideTabs: TabsWidget) {
        sideTabs.setMainTab(this)
        if (multiRecipeTypes.size > 1) sideTabs.attachSubTab(GTLAddMachineModeFancyConfigurator(this))
        CombinedDirectionalFancyConfigurator.of(self(), self())?.let { sideTabs.attachSubTab(it) }
    }

    override val multiRecipeTypes: Array<GTRecipeType> = arrayOf()

    override fun addDisplayText(textList: MutableList<Component>) {
        MultiblockDisplayText.builder(textList, isFormed())
            .setWorkingStatus(recipeLogic.isWorkingEnabled, recipeLogic.isActive)
            .addEnergyUsageLine(energyContainer)
            .addEnergyTierLine(tier)
            .addMachineModeLine(multiRecipeType)
            .addParallelsLine(maxParallel)
            .addWorkingStatusLine()
            .addProgressLine(recipeLogic.progressPercent)
            .addRecipeStatus(recipeLogic as IRecipeStatus)
        this.definition.additionalDisplay.accept(this, textList)
    }
}
