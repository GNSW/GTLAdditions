---
navigation:
  title: 原子置换核心
  icon: atomic_transmutation_core
  parent: controller/multiblock_controller.md
  position: 10
item_ids:
  - atomic_transmutation_core
---

# 原子置换核心

<BlockImage id = "atomic_transmutation_core" scale = "8"/>

* 需要安装 <ItemLink id="gtladditions:me_block_conservation" /> 来使用
* 可以在机器的gui界面中放入(高速或极限)转换卡
* 机器成型后会一直空转 ~~(不是bug而是特性)~~, 在每次工作结束后会转换**嬗变总线**内的方块
* 耗能为机器当前电压等级对应功率
* 每次单个方块转换数量为并行数
* 安装极限转换卡时, 可以一次性转换多种方块, 同时工作耗时变成1秒

> 转换卡可提供 `e^电压等级` 并行 \
> 高速转换卡可提供 `4^电压等级` 并行 \
> 极限转换卡可提供 `5.7^电压等级` 并行

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

