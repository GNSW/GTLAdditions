package com.gtladd.gtladditions.common.register

import com.gregtechceu.gtceu.api.item.ComponentItem
import com.gregtechceu.gtceu.common.data.GTItems

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

import com.gtladd.gtladditions.api.registry.GTLAddRegistration
import com.gtladd.gtladditions.common.data.GTLAddCreativeModeTabs
import com.gtladd.gtladditions.common.items.GuideBook
import com.tterrag.registrate.util.entry.ItemEntry
import com.tterrag.registrate.util.nullness.NonNullBiConsumer

object GTLAddItems {
    @JvmField
    var ECHO_SHARD_BOULE: ItemEntry<Item>

    @JvmField
    var ECHO_SHARD_WAFER: ItemEntry<Item>

    @JvmField
    var BIOWARE_ECHO_SHARD_BOULE: ItemEntry<Item>

    @JvmField
    var OUTSTANDING_SOC_WAFER: ItemEntry<Item>

    @JvmField
    var OUTSTANDING_SOC: ItemEntry<Item>

    @JvmField
    var HASSIUM_BOULE: ItemEntry<Item>

    @JvmField
    var HASSIUM_WAFER: ItemEntry<Item>

    @JvmField
    var PREPARE_EXTRAORDINARY_SOC_WAFER: ItemEntry<Item>

    @JvmField
    var EXTRAORDINARY_SOC_WAFER: ItemEntry<Item>

    @JvmField
    var EXTRAORDINARY_SOC: ItemEntry<Item>

    @JvmField
    var STARMETAL_BOULE: ItemEntry<Item>

    @JvmField
    var STARMETAL_WAFER: ItemEntry<Item>

    @JvmField
    var DRAGON_ELEMENT_STARMETAL_WAFER: ItemEntry<Item>

    @JvmField
    var CHAOS_SOC_WAFER: ItemEntry<Item>

    @JvmField
    var CHAOS_SOC: ItemEntry<Item>

    @JvmField
    var PERIODICIUM_BOULE: ItemEntry<Item>

    @JvmField
    var PERIODICIUM_WAFER: ItemEntry<Item>

    @JvmField
    var PREPARE_SPACETIME_SOC_WAFER: ItemEntry<Item>

    @JvmField
    var SPACETIME_SOC_WAFER: ItemEntry<Item>

    @JvmField
    var SPACETIME_SOC: ItemEntry<Item>

    @JvmField
    var INFINITY_BOULE: ItemEntry<Item>

    @JvmField
    var INFINITY_WAFER: ItemEntry<Item>

    @JvmField
    var PREPARE_PRIMARY_SOC_WAFER: ItemEntry<Item>

    @JvmField
    var PRIMARY_SOC_WAFER: ItemEntry<Item>

    @JvmField
    var PRIMARY_SOC: ItemEntry<Item>

    @JvmField
    var SPACETIME_LENS: ItemEntry<Item>

    @JvmField
    val BARNARDA_DATA: ItemEntry<Item>

    @JvmField
    val ULTIMATE_CONVERSATION_CARD: ItemEntry<Item>
    val HARMONIZING_CORE: ItemEntry<Item>

    @JvmField
    val GUIDE_BOOK: ItemEntry<ComponentItem>

    @JvmStatic
    fun init() {}

    private fun register(id: String, name: String): ItemEntry<Item> {
        return GTLAddRegistration.REGISTRATE.item(id, ::Item).lang(name).register()
    }

    init {
        GTLAddRegistration.REGISTRATE.creativeModeTab { GTLAddCreativeModeTabs.GTLADD_ITEMS }
        SPACETIME_LENS = register("spacetime_lens", "Spacetime Lens")
        ECHO_SHARD_BOULE = register("echo_shard_boule", "Echo Shard Boule")
        ECHO_SHARD_WAFER = register("echo_shard_wafer", "Echo Shard Wafer")
        BIOWARE_ECHO_SHARD_BOULE = register("bioware_echo_shard_wafer", "Bioware Echo Shard Wafer")
        OUTSTANDING_SOC_WAFER = register("outstanding_soc_wafer", "Outstanding Soc Wafer")
        OUTSTANDING_SOC = register("outstanding_soc", "Outstanding Soc")
        HASSIUM_BOULE = register("hassium_boule", "Hassium Boule")
        HASSIUM_WAFER = register("hassium_wafer", "Hassium Wafer")
        PREPARE_EXTRAORDINARY_SOC_WAFER = register("prepare_extraordinary_soc_wafer", "Prepare Extraordinary Soc Wafer")
        EXTRAORDINARY_SOC_WAFER = register("extraordinary_soc_wafer", "Extraordinary Soc Wafer")
        EXTRAORDINARY_SOC = register("extraordinary_soc", "Extraordinary Soc")
        STARMETAL_BOULE = register("starmetal_boule", "Starmetal Boule")
        STARMETAL_WAFER = register("starmetal_wafer", "Starmetal Wafer")
        DRAGON_ELEMENT_STARMETAL_WAFER = register("dragon_element_starmetal_wafer", "Dragon Element Starmetal Wafer")
        CHAOS_SOC_WAFER = register("chaos_soc_wafer", "Chaos Soc Wafer")
        CHAOS_SOC = register("chaos_soc", "Chaos Soc")
        PERIODICIUM_BOULE = register("periodicium_boule", "Periodicuim Boule")
        PERIODICIUM_WAFER = register("periodicium_wafer", "Periodicuim Wafer")
        PREPARE_SPACETIME_SOC_WAFER = register("prepare_spacetime_soc_wafer", "Prepare Spacetime Soc Wafer")
        SPACETIME_SOC_WAFER = register("spacetime_soc_wafer", "Spacetime Soc Wafer")
        SPACETIME_SOC = register("spacetime_soc", "Spacetime Soc")
        INFINITY_BOULE = register("infinity_boule", "Infinity Boule")
        INFINITY_WAFER = register("infinity_wafer", "Infinity Wafer")
        PREPARE_PRIMARY_SOC_WAFER = register("prepare_primary_soc_wafer", "Prepare Primary Soc Wafer")
        PRIMARY_SOC_WAFER = register("primary_soc_wafer", "Primary Soc Wafer")
        PRIMARY_SOC = register("primary_soc", "Primary Soc")
        BARNARDA_DATA = register("barnarda_data", "Barnarda Data")
        ULTIMATE_CONVERSATION_CARD = register("ultimate_conversation_card", "Ultimate Conversation Card")
        HARMONIZING_CORE = GTLAddRegistration.Companion.REGISTRATE.item("harmonizing_core") {
            object : Item(it) {
                override fun isFoil(stack: ItemStack): Boolean {
                    return true
                }
            } as Item
        }.lang("Harmonizing Core").register()
        GUIDE_BOOK = GTLAddRegistration.Companion.REGISTRATE.item("guide", ComponentItem::create)
            .onRegister(GTItems.attach(GuideBook())).model(NonNullBiConsumer.noop()).register()
    }
}
