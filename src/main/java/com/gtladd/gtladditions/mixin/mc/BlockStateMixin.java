package com.gtladd.gtladditions.mixin.mc;

import org.gtlcore.gtlcore.common.data.GTLItems;

import com.lowdragmc.lowdraglib.async.AsyncThreadData;
import com.lowdragmc.lowdraglib.async.IAsyncLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import com.google.common.collect.ImmutableMap;
import com.gtladd.gtladditions.api.async.AsyncFluidTransform;
import com.gtladd.gtladditions.common.register.GTLAddMaterial;
import com.gtladd.gtladditions.utils.MathUtil;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockState.class)
public abstract class BlockStateMixin extends BlockBehaviour.BlockStateBase {

    private static final Long2ObjectOpenHashMap<IAsyncLogic> FLUID_TRANSFORM_ASYNC_MAP = new Long2ObjectOpenHashMap<>();

    protected BlockStateMixin(Block owner, ImmutableMap<Property<?>, Comparable<?>> values, MapCodec<BlockState> propertiesCodec) {
        super(owner, values, propertiesCodec);
    }

    @Override
    public void onPlace(Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(level, pos, oldState, movedByPiston);
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!this.getFluidState().isEmpty()) {
            if (!this.getFluidState().isSource()) return;
            if (this.getFluidState().is(GTLAddMaterial.MINING_ESSENCE.getFluid())) {
                var itemStack = MathUtil.INSTANCE.random(100) >= 30 ? GTLItems.MINING_CRYSTAL.asStack() : ItemStack.EMPTY;
                var task = new AsyncFluidTransform(serverLevel, pos, itemStack);
                FLUID_TRANSFORM_ASYNC_MAP.put(pos.asLong(), task);
                AsyncThreadData.getOrCreate(serverLevel).addAsyncLogic(task);
            } else if (this.getFluidState().is(GTLAddMaterial.TREASURES_ESSENCE.getFluid())) {
                var itemStack = MathUtil.INSTANCE.random(100) >= 30 ? GTLItems.TREASURES_CRYSTAL.asStack() : ItemStack.EMPTY;
                var task = new AsyncFluidTransform(serverLevel, pos, itemStack);
                FLUID_TRANSFORM_ASYNC_MAP.put(pos.asLong(), task);
                AsyncThreadData.getOrCreate(serverLevel).addAsyncLogic(task);
            }
        } else if (!oldState.getFluidState().isEmpty()) {
            var fluidState = oldState.getFluidState();
            if (fluidState.isSource() && (fluidState.is(GTLAddMaterial.MINING_ESSENCE.getFluid()) || fluidState.is(GTLAddMaterial.TREASURES_ESSENCE.getFluid()))) {
                var task = FLUID_TRANSFORM_ASYNC_MAP.remove(pos.asLong());
                if (task != null) AsyncThreadData.getOrCreate(serverLevel).removeAsyncLogic(task);
            }
        }
    }
}
