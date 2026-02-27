package com.gtladd.gtladditions.client.render.machine

import org.gtlcore.gtlcore.GTLCore
import org.gtlcore.gtlcore.client.ClientUtil
import org.gtlcore.gtlcore.utils.RenderUtil

import com.gregtechceu.gtceu.GTCEu
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer

import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.Direction.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.model.data.ModelData

import com.gtladd.gtladditions.GTLAdditions
import com.mojang.blaze3d.vertex.PoseStack
import org.joml.Quaternionf

import java.util.function.Consumer

class SuperFactoryRender() : WorkableCasingMachineRenderer(
    GTLCore.id("block/multi_functional_casing"),
    GTCEu.id("block/multiblock/gcym/large_assembler")
) {

    @OnlyIn(Dist.CLIENT)
    override fun render(blockEntity: BlockEntity, partialTicks: Float, poseStack: PoseStack, buffer: MultiBufferSource, combinedLight: Int, combinedOverlay: Int) {
        (blockEntity as IMachineBlockEntity).let { machineBlockEntity ->
            machineBlockEntity.let { it.metaMachine as WorkableElectricMultiblockMachine }.let { machine ->
                if (machine.isActive) {
                    val tick = RenderUtil.getSmoothTick(machine, partialTicks)
                    var x = 0.5
                    var y = 0.5
                    var z = 0.5
                    when (machine.frontFacing) {
                        NORTH -> z = 16.5
                        SOUTH -> z = -15.5
                        WEST -> x = 16.5
                        EAST -> x = -15.5
                        UP -> y = -15.5
                        DOWN -> y = 16.5
                    }
                    poseStack.pushPose()
                    poseStack.translate(x, y, z)
                    renderEmoji(tick, poseStack, buffer)
                    poseStack.popPose()
                }
            }
        }
    }

    override fun onAdditionalModel(registry: Consumer<ResourceLocation>) {
        super.onAdditionalModel(registry)
        registry.accept(EMOJI)
    }

    @OnlyIn(Dist.CLIENT)
    override fun hasTESR(blockEntity: BlockEntity): Boolean = true

    @OnlyIn(Dist.CLIENT)
    override fun isGlobalRenderer(blockEntity: BlockEntity): Boolean = true

    @OnlyIn(Dist.CLIENT)
    override fun getViewDistance(): Int = 512

    companion object {
        val EMOJI: ResourceLocation = GTLAdditions.id("obj/emoji")
        private fun renderEmoji(tick: Float, poseStack: PoseStack, buffer: MultiBufferSource) {
            val scale = 0.01f * 15.5f
            poseStack.pushPose()
            poseStack.scale(scale, scale, scale)
            poseStack.mulPose(Quaternionf().fromAxisAngleDeg(0f, 1f, 1f, (tick / 2) % 360f))
            ClientUtil.modelRenderer().renderModel(
                poseStack.last(), buffer.getBuffer(RenderType.translucent()), null,
                ClientUtil.getBakedModel(EMOJI),
                1.0f, 1.0f, 1.0f,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY, RenderType.translucent()
            )
            poseStack.popPose()
        }
    }
}
