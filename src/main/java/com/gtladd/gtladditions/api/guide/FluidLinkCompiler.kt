package com.gtladd.gtladditions.api.guide

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper
import com.lowdragmc.lowdraglib.side.fluid.FluidStack

import com.gtladd.gtladditions.api.gui.GTLytSlotGrid
import com.gtladd.gtladditions.utils.Registries.getFluid
import guideme.compiler.PageCompiler
import guideme.compiler.tags.FlowTagCompiler
import guideme.document.flow.LytFlowParent
import guideme.document.flow.LytTooltipSpan
import guideme.libs.mdast.mdx.model.MdxJsxElementFields

class FluidLinkCompiler : FlowTagCompiler() {

    override fun getTagNames() = mutableSetOf("FluidLink")

    override fun compile(compiler: PageCompiler, parent: LytFlowParent, el: MdxJsxElementFields) {
        val string = el.getAttributeString("id", "")
        if (string.isEmpty()) {
            parent.appendError(compiler, "No found for fluid id: ", el)
            return
        }
        val fluidStack = FluidStack.create(string.getFluid, 1000)
        val span = LytTooltipSpan()
        span.modifyStyle { it.bold(true) }
        span.appendComponent(FluidHelper.getDisplayName(fluidStack))
        span.setTooltip(GTLytSlotGrid.FluidTooltip(fluidStack))
        parent.append(span)
    }
}
