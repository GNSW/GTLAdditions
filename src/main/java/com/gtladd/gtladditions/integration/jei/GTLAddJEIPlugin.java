package com.gtladd.gtladditions.integration.jei;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.gtladd.gtladditions.GTLAdditions;
import com.gtladd.gtladditions.common.register.GTLAddMaterial;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IExtraIngredientRegistration;

import java.util.List;

@JeiPlugin
public class GTLAddJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return GTLAdditions.id("jei_plugin");
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        registration.addExtraItemStacks(List.of(
                new ItemStack(GTLAddMaterial.MINING_ESSENCE.getBucket()),
                new ItemStack(GTLAddMaterial.TREASURES_ESSENCE.getBucket()),
                new ItemStack(GTLAddMaterial.CRYSTALLINE_PROTOPLASM.getBucket())));
    }
}
