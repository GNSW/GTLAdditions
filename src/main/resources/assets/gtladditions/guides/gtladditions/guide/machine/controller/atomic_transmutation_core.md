---
navigation:
  title: Atomic Transmutation Core
  icon: atomic_transmutation_core
  parent: controller/multiblock_controller.md
  position: 10
item_ids:
  - atomic_transmutation_core
---

# Atomic Transmutation Core

<BlockImage id = "atomic_transmutation_core" scale = "8"/>

* Requires <ItemLink id="gtladditions:me_block_conservation" /> to be installed to use
* Can insert (Fast or Ultimate) Conversion cards in the machine's GUI
* The machine will idle continuously after formation ~~(not a bug but a feature)~~, converting blocks in the **transmutation bus** after each work cycle
* Energy consumption corresponds to the power of the machine's current voltage tier
* Number of blocks converted per operation equals the parallel count
* When using Ultimate conversion card, can convert multiple blocks at once, with work time reduced to 1 second

> Conversion card provides `e^voltage tier` parallel \
> Fast Conversion card provides `4^voltage tier` parallel \
> Ultimate Conversion card provides `5.7^voltage tier` parallel

<Row>
    <Recipe id="gtladditions:transmutation_block_conversion/block.kubejs.draconium_block_charged" />

    <Recipe id="gtladditions:transmutation_block_conversion/block.minecraft.moss_block" />

    <Recipe id="gtladditions:transmutation_block_conversion/block.minecraft.warped_stem" />

    <Recipe id="gtladditions:transmutation_block_conversion/block.minecraft.sculk" />
</Row>

<Row>
    <Recipe id="gtladditions:transmutation_block_conversion/block.minecraft.crimson_stem" />

    <Recipe id="gtladditions:transmutation_block_conversion/block.minecraft.bone_block" />

    <Recipe id="gtladditions:transmutation_block_conversion/block.kubejs.essence_block" />
</Row>

