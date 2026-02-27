package com.gtladd.gtladditions.api.gui

import org.gtlcore.gtlcore.utils.NumberUtils

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.api.recipe.content.Content
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient
import com.gregtechceu.gtceu.client.TooltipsHandler
import com.gregtechceu.gtceu.utils.FormattingUtil

import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper
import com.lowdragmc.lowdraglib.side.fluid.FluidStack
import com.lowdragmc.lowdraglib.utils.LocalizationUtils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

import appeng.client.gui.me.common.StackSizeRenderer
import com.gtladd.gtladditions.utils.GTRecipeUtils.amount
import com.gtladd.gtladditions.utils.GTRecipeUtils.stack
import guideme.document.LytRect
import guideme.document.block.LytBlock
import guideme.document.block.LytSlotGrid
import guideme.document.interaction.GuideTooltip
import guideme.document.interaction.InteractiveElement
import guideme.document.interaction.ItemTooltip
import guideme.internal.GuideME
import guideme.layout.LayoutContext
import guideme.render.GuiAssets
import guideme.render.GuiSprite
import guideme.render.RenderContext
import guideme.siteexport.ResourceExporter
import it.unimi.dsi.fastutil.objects.ObjectArrayList

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.min

class GTLytSlotGrid(private val width: Int, private val itemHeight: Int, fluidHeight: Int) : LytSlotGrid(0, 0) {
    private val height: Int = itemHeight + fluidHeight
    private val slots: Array<GTLytSlot?> = arrayOfNulls<GTLytSlot>(width * height)

    private fun addSlot(x: Int, y: Int, content: Content) {
        if (x > this.width || x < 0) throw IndexOutOfBoundsException("x: $x")
        if (y > this.height || y < 0) throw IndexOutOfBoundsException("y: $y")
        val index = getSlotIndex(x, y)
        var s = this.slots[index]
        s.takeIf { it != null }?.let {
            it.removeChild(it)
            this.slots[index] = null
        }
        this.slots[index] = GTLytSlot(content)
        s = this.slots[index]
        this.append(s)
    }

    override fun computeBoxLayout(context: LayoutContext, x: Int, y: Int, availableWidth: Int): LytRect {
        for (row in 0..<this.height) {
            for (col in 0..<this.width) {
                val index: Int = this.getSlotIndex(col, row)
                if (index < this.slots.size) {
                    this.slots[index]?.layout(context, x + col * 18, y + row * 18, availableWidth)
                }
            }
        }
        return LytRect(x, y, 18 * this.width, 18 * this.height)
    }

    override fun render(context: RenderContext) {
        for (y in 0..<this.height) {
            for (x in 0..<this.width) {
                val index: Int = this.getSlotIndex(x, y)
                if (this.slots[index] == null) {
                    val b = if (y < itemHeight) GuiAssets.SLOT_BACKGROUND else FLUID_SLOT
                    context.drawIcon(this.bounds.x() + 18 * x, this.bounds.y() + 18 * y, b)
                }
            }
        }
        super.render(context)
    }

    private fun getSlotIndex(col: Int, row: Int) = row * this.width + col

    class Builder(private val recipe: GTRecipe) {

        val recipeInput: GTLytSlotGrid
            get() {
                var width = 0
                var itemHeight = 0
                var fluidHeight = 0
                for (value in recipe.recipeType.maxInputs.entries) {
                    if (value.key.doRenderSlot) {
                        val `val` = value.value
                        if (`val` > width) width = min(`val`, 3)
                        if (value.key === ItemRecipeCapability.CAP) {
                            itemHeight = (`val` + 2) / 3
                        } else if (value.key === FluidRecipeCapability.CAP) {
                            fluidHeight = (`val` + 2) / 3
                        }
                    }
                }
                var iI = 0
                var fI = 0
                val slot = GTLytSlotGrid(width, itemHeight, fluidHeight)
                for (entry in recipe.inputs.entries) {
                    if (entry.key === ItemRecipeCapability.CAP) {
                        for (c in entry.value) {
                            slot.addSlot(iI % 3, iI / 3, c)
                            iI++
                        }
                    } else if (entry.key === FluidRecipeCapability.CAP) {
                        for (c in entry.value) {
                            slot.addSlot(fI % 3, fI / 3 + itemHeight, c)
                            fI++
                        }
                    }
                }
                return slot
            }

        val recipeOutput: GTLytSlotGrid
            get() {
                var width = 0
                var itemHeight = 0
                var fluidHeight = 0
                for (value in recipe.recipeType.maxOutputs.entries) {
                    if (value.key.doRenderSlot) {
                        val `val` = value.value
                        if (`val` > width) width = min(`val`, 3)
                        if (value.key === ItemRecipeCapability.CAP) {
                            itemHeight = (`val` + 2) / 3
                        } else if (value.key === FluidRecipeCapability.CAP) {
                            fluidHeight = (`val` + 2) / 3
                        }
                    }
                }
                var iI = 0
                var fI = 0
                val slot = GTLytSlotGrid(width, itemHeight, fluidHeight)
                for (entry in recipe.outputs.entries) {
                    if (entry.key === ItemRecipeCapability.CAP) {
                        for (c in entry.value) {
                            slot.addSlot(iI % 3, iI / 3, c)
                            iI++
                        }
                    } else if (entry.key === FluidRecipeCapability.CAP) {
                        for (c in entry.value) {
                            slot.addSlot(fI % 3, fI / 3 + itemHeight, c)
                            fI++
                        }
                    }
                }
                return slot
            }
    }

    private class GTLytSlot(private val content: Content) : LytBlock(), InteractiveElement {
        override fun computeLayout(layoutContext: LayoutContext, x: Int, y: Int, availableWidth: Int) = LytRect(x, y, 18, 18)

        override fun onLayoutMoved(x: Int, y: Int) = Unit

        override fun renderBatch(renderContext: RenderContext, multiBufferSource: MultiBufferSource) = Unit

        override fun render(context: RenderContext) {
            val g = context.guiGraphics()
            val x = bounds.x()
            val y = bounds.y()
            val width = bounds.width()
            val height = bounds.height()
            val font = Minecraft.getInstance().font

            val ing = content.content
            when (ing) {
                is Ingredient -> {
                    val item = getDisplayedItemStack(ing.items)
                    context.fillIcon(bounds, GuiAssets.SLOT)
                    if (!item.isEmpty) {
                        val pose = context.poseStack()
                        pose.pushPose()
                        pose.translate(x + 1f, y + 1f, 2f)
                        g.renderItem(item, 0, 0)
                        pose.popPose()
                        if (ing.amount > 1) StackSizeRenderer.renderSizeLabel(g, font, x + 1f, y + 1f, ing.amount.toString(), true)
                    }
                }
                is FluidIngredient -> {
                    val fluid = getDisplayedFluidStack(ing.getStacks())
                    context.fillIcon(bounds, FLUID_SLOT)
                    if (!fluid.isEmpty) {
                        DrawerHelper.drawFluidForGui(g, fluid, ing.amount, x + 1, y + 1, width - 2, height - 2)
                        g.pose().pushPose()
                        g.pose().translate(0f, 0f, 400f)
                        g.pose().scale(.5f, .5f, 1f)
                        var amount = fluid.amount
                        val s: String
                        if (amount >= 1000) {
                            amount /= 1000
                            s = NumberUtils.formatLong(amount) + "B"
                        } else {
                            s = "${amount}mB"
                        }
                        g.drawString(font, s, ((x + (width / 3f)) * 2 - font.width(s) + 21).toInt(), ((y + (height / 3f) + 6) * 2).toInt(), 0xFFFFFF, true)
                        g.pose().popPose()
                    }
                }
            }

            if (content.chance != 10000) {
                g.pose().pushPose()
                g.pose().translate(.0f, .0f, 400f)
                g.pose().scale(.5f, .5f, 1f)
                val chance = 100f * content.chance.toFloat() / content.maxChance.toFloat()
                val percent = FormattingUtil.formatPercent(chance.toDouble())
                val s = if (chance == 0f) LocalizationUtils.format("gtceu.gui.content.chance_0_short") else "$percent%"
                val xDraw = ((x + width / 3f) * 2f - font.width(s) + 23f).toInt() - (if (chance == 0f) 10 else 0)
                val yDraw = ((y + height / 3f + 6f) * 2.0f - height).toInt() - (if (chance == 0f) 3 else 0)
                g.drawString(font, s, xDraw, yDraw, if (chance == 0f) 16711680 else 16776960, true)
                g.pose().popPose()
            }
        }

        override fun getTooltip(x: Float, y: Float): Optional<GuideTooltip> {
            val ing = content.content
            when (ing) {
                is Ingredient -> {
                    val stack = ing.`kjs$getFirst`()
                    return if (stack.isEmpty) Optional.empty() else Optional.of(ItemTooltip(stack))
                }
                is FluidIngredient -> {
                    val stack = ing.stack
                    return if (stack.isEmpty) Optional.empty() else Optional.of(FluidTooltip(stack))
                }
            }
            return Optional.empty()
        }

        private fun getDisplayedItemStack(stack: Array<ItemStack>) = if (stack.isEmpty()) {
            ItemStack.EMPTY
        } else {
            stack[(System.nanoTime() / TimeUnit.MILLISECONDS.toNanos(2000L) % stack.size.toLong()).toInt()]
        }

        private fun getDisplayedFluidStack(stack: Array<FluidStack>) = if (stack.isEmpty()) {
            FluidStack.empty()
        } else {
            stack[(System.nanoTime() / TimeUnit.MILLISECONDS.toNanos(2000L) % stack.size.toLong()).toInt()]
        }
    }

    @JvmRecord
    data class FluidTooltip(val fluidStack: FluidStack) : GuideTooltip {
        override fun getLines(): MutableList<ClientTooltipComponent> {
            val list = ObjectArrayList<Component>()
            list.add(FluidHelper.getDisplayName(fluidStack))
            TooltipsHandler.appendFluidTooltips(fluidStack.fluid, fluidStack.amount, list::add, null)
            return list.stream().map { it!!.visualOrderText }.map { ClientTooltipComponent.create(it) }.toList()
        }

        override fun getIcon(): ItemStack = fluidStack.fluid.bucket.defaultInstance

        override fun exportResources(resourceExporter: ResourceExporter) = resourceExporter.referenceFluid(fluidStack.fluid)
    }

    companion object {
        private val FLUID_SLOT: GuiSprite = GuiAssets.sprite(GuideME.makeId("fluid_slot"))

        @JvmStatic
        fun builder(recipe: GTRecipe): Builder = Builder(recipe)
    }
}
