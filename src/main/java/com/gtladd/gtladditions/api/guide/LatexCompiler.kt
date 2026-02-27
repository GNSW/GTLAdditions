package com.gtladd.gtladditions.api.guide

import guideme.compiler.PageCompiler
import guideme.compiler.tags.BlockTagCompiler
import guideme.document.block.LytBlockContainer
import guideme.libs.mdast.mdx.model.MdxJsxElementFields
import guideme.libs.mdast.model.MdAstNode

class LatexCompiler : BlockTagCompiler() {

    override fun getTagNames() = mutableSetOf("Latex")

    override fun compile(compiler: PageCompiler, parent: LytBlockContainer, el: MdxJsxElementFields) {
        val string = el.getAttributeString("math", "")
        if (string.isEmpty()) {
            parent.appendError(compiler, "Latex tag requires 'math' attribute", el)
            return
        }
        val latex = LytLatex(string)
        latex.sourceNode = el as MdAstNode
        parent.append(latex)
    }
}
