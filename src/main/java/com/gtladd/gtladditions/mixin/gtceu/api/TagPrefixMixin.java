package com.gtladd.gtladditions.mixin.gtceu.api;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.data.tag.TagType;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(TagPrefix.class)
@SuppressWarnings("all")
public class TagPrefixMixin {

    private TagKey<Item>[] tagKeys;

    @Shadow(remap = false)
    @Final
    protected List<TagType> tags;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public TagKey<Item>[] getItemParentTags() {
        if (this.tagKeys == null) {
            this.tagKeys = this.tags.stream().filter(TagType::isParentTag)
                    .map((type) -> type.getTag((TagPrefix) (Object) this, null)).toArray(TagKey[]::new);
        }
        return this.tagKeys;
    }
}
