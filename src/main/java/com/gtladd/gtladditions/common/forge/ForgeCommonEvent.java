package com.gtladd.gtladditions.common.forge;

import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.config.ConfigHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.gtladd.gtladditions.common.register.GTLAddMaterial;
import com.gtladd.gtladditions.utils.TooltipsUtil;

public class ForgeCommonEvent {

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {
        if (ConfigHolder.INSTANCE.enableSkyBlokeMode) {
            TooltipsUtil.INSTANCE.addItemTooltips(GTLAddMaterial.MINING_ESSENCE.getBucket(),
                    Component.translatable("tooltip.gtladditions.item.mining_essence_bucket.0").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD),
                    Component.translatable("tooltip.gtladditions.item.mining_essence_bucket.1").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD),
                    Component.translatable("tooltip.gtladditions.item.mining_essence_bucket.2").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD));
            TooltipsUtil.INSTANCE.addItemTooltips(GTLAddMaterial.TREASURES_ESSENCE.getBucket(),
                    Component.translatable("tooltip.gtladditions.item.treasures_essence_bucket.0").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
                    Component.translatable("tooltip.gtladditions.item.treasures_essence_bucket.1").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
                    Component.translatable("tooltip.gtladditions.item.treasures_essence_bucket.2").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD));
            TooltipsUtil.INSTANCE.addItemTooltips(GTLAddMaterial.CRYSTALLINE_PROTOPLASM.getBucket(),
                    Component.translatable("tooltip.gtladditions.item.crystalline_protoplasm_bucket").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD));
            TooltipsUtil.INSTANCE.addItemTooltips(GTLItems.MINING_CRYSTAL.asItem(),
                    Component.translatable("tooltip.gtladditions.add.0").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD),
                    Component.translatable("tooltip.gtladditions.item.mining_crystal.0").withStyle(ChatFormatting.DARK_GRAY),
                    Component.translatable("tooltip.gtladditions.item.mining_crystal.1").withStyle(ChatFormatting.DARK_GRAY));
            TooltipsUtil.INSTANCE.addItemTooltips(GTLItems.TREASURES_CRYSTAL.asItem(),
                    Component.translatable("tooltip.gtladditions.add.0").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD),
                    Component.translatable("tooltip.gtladditions.item.treasures_crystal.0").withStyle(ChatFormatting.DARK_PURPLE),
                    Component.translatable("tooltip.gtladditions.item.treasures_crystal.1").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    @SubscribeEvent
    public static void ItemTooltipEvent(ItemTooltipEvent event) {
        TooltipsUtil.INSTANCE.setItemTooltips(event.getItemStack().getItem(), event.getToolTip());
    }
}
