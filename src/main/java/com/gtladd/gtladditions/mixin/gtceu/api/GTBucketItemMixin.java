package com.gtladd.gtladditions.mixin.gtceu.api;

import com.gregtechceu.gtceu.api.item.GTBucketItem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Supplier;

@Mixin(GTBucketItem.class)
public abstract class GTBucketItemMixin extends BucketItem {

    public GTBucketItemMixin(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    /**
     * @author .
     * @reason 可以倒
     */
    @Overwrite(remap = false)
    public boolean emptyContents(Player pPlayer, Level pLevel, BlockPos pPos,
                                 BlockHitResult pResult, ItemStack container) {
        return super.emptyContents(pPlayer, pLevel, pPos, pResult, container);
    }
}
