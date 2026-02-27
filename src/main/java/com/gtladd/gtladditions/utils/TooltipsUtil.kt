package com.gtladd.gtladditions.utils

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

object TooltipsUtil {
    val ITEM_TOOLTIPS_MAP = Object2ObjectOpenHashMap<Item, List<Component>>()

    fun Item.addItemTooltips(vararg component: Component) {
        val t = ITEM_TOOLTIPS_MAP[this]
        if (t == null) ITEM_TOOLTIPS_MAP.put(this, component.toList())
    }

    fun Item.setItemTooltips(tooltips: MutableList<Component>) {
        ITEM_TOOLTIPS_MAP[this]?.let { tooltips.addAll(it) }
    }
}
