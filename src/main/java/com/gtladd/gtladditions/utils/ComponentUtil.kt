package com.gtladd.gtladditions.utils

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

object ComponentUtil {

    val String.translatable: String get() = this.toComponent.string
    val String.toComponent: MutableComponent get() = Component.translatable(this)
    val String.literal: MutableComponent get() = Component.literal(this)
    val Number.literal: MutableComponent get() = this.toString().literal
}
