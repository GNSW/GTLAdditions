package com.gtladd.gtladditions.mixin.gtceu.integration.jade;

import org.gtlcore.gtlcore.api.machine.ISteamMachine;
import org.gtlcore.gtlcore.utils.NumberUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.integration.jade.provider.RecipeLogicProvider;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.gtladd.gtladditions.api.recipe.IWirelessGTRecipe;
import com.gtladd.gtladditions.utils.MathUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static net.minecraft.ChatFormatting.*;
import static org.gtlcore.gtlcore.utils.TextUtil.GTL_CORE$VC;

@Mixin(value = RecipeLogicProvider.class, priority = 1100)
public abstract class RecipeLogicProviderMixin extends CapabilityBlockProvider<RecipeLogic> {

    protected RecipeLogicProviderMixin(ResourceLocation uid) {
        super(uid);
    }

    @Inject(method = "write(Lnet/minecraft/nbt/CompoundTag;Lcom/gregtechceu/gtceu/api/machine/trait/RecipeLogic;)V",
            at = @At("HEAD"),
            remap = false)
    protected void write(CompoundTag data, RecipeLogic capability, CallbackInfo ci) {
        if (capability.getLastRecipe() instanceof IWirelessGTRecipe wirelessGTRecipe && wirelessGTRecipe.getWirelessEUt() != 0d) {
            var recipeData = new CompoundTag();
            recipeData.putDouble("wirelessEUt", wirelessGTRecipe.getWirelessEUt());
            recipeData.putBoolean("io", wirelessGTRecipe.getIO() == IO.IN);
            data.put("recipe_data", recipeData);
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        if (capData.getBoolean("Working")) {
            var recipeInfo = capData.getCompound("Recipe");
            var recipeData = capData.getCompound("recipe_data");
            if (!recipeData.isEmpty()) {
                var EU = recipeData.getDouble("wirelessEUt");
                var IO = recipeData.getBoolean("io");
                var longEU = MathUtil.INSTANCE.getSafeToLong(EU);
                if (longEU > 0) {
                    long absEUt = Math.abs(longEU);
                    var tier = GTUtil.getTierByVoltage(absEUt);
                    var text = Component.literal(NumberUtils.formatDouble(EU)).withStyle(RED)
                            .append(Component.literal(" EU/t").withStyle(RESET)
                                    .append(Component.literal(" (").withStyle(GREEN)
                                            .append(Component.translatable("gtceu.top.electricity",
                                                    MathUtil.INSTANCE.format(EU / GTValues.V[tier], 2), GTValues.VNF[tier])
                                                    .withStyle(style -> style.withColor(GTL_CORE$VC[tier])))
                                            .append(Component.literal(")").withStyle(GREEN))));
                    if (IO) tooltip.add(Component.translatable("gtceu.top.energy_consumption").append(" ").append(text));
                    else tooltip.add(Component.translatable("gtceu.top.energy_production").append(" ").append(text));
                }
            } else if (!recipeInfo.isEmpty()) {
                var EUt = recipeInfo.getLong("EUt");
                var isInput = recipeInfo.getBoolean("isInput");
                boolean isSteam = false;
                if (EUt != 0) {
                    if (blockEntity instanceof MetaMachineBlockEntity mbe) {
                        var machine = mbe.getMetaMachine();
                        if (machine instanceof ISteamMachine steamMachine) {
                            EUt = (long) Math.ceil(EUt * steamMachine.getConversionRate());
                            isSteam = true;
                        }
                    }
                    MutableComponent text;
                    if (isSteam) text = Component.literal(FormattingUtil.formatNumbers(EUt) + " mB/t").withStyle(ChatFormatting.GREEN);
                    else {
                        long absEUt = Math.abs(EUt);
                        var tier = GTUtil.getTierByVoltage(absEUt);
                        text = Component.literal(NumberUtils.formatLong(absEUt)).withStyle(RED)
                                .append(Component.literal(" EU/t").withStyle(RESET)
                                        .append(Component.literal(" (").withStyle(GREEN)
                                                .append(Component
                                                        .translatable("gtceu.top.electricity",
                                                                FormattingUtil.formatNumber2Places(absEUt / ((float) GTValues.V[tier])),
                                                                GTValues.VNF[tier])
                                                        .withStyle(style -> style.withColor(GTL_CORE$VC[tier])))
                                                .append(Component.literal(")").withStyle(GREEN))));
                    }
                    if (isInput) {
                        var component = isSteam ? Component.translatable("gtceu.jade.steam_consumption") : Component.translatable("gtceu.top.energy_consumption");
                        tooltip.add(component.append(" ").append(text));
                    } else tooltip.add(Component.translatable("gtceu.top.energy_production").append(" ").append(text));
                }
            }
            String reason = capData.getString("work_reason");
            if (reason.isEmpty()) return;
            Component reasonComponent = Component.Serializer.fromJson(reason);
            if (reasonComponent == null) return;
            tooltip.add(Component.translatable("gtceu.recipe.fail.reason", reasonComponent).withStyle(RED));
        } else {
            String reason = capData.getString("reason");
            if (reason.isEmpty()) return;
            Component reasonComponent = Component.Serializer.fromJson(reason);
            if (reasonComponent == null) return;
            tooltip.add(Component.translatable("gtceu.recipe.fail.reason", reasonComponent).withStyle(RED));
        }
    }
}
