---
navigation:
  title: 反熵冷凝中枢
  icon: antientropy_condensation_center
  parent: controller/multiblock_controller.md
  position: 10
item_ids:
  - antientropy_condensation_center
---

# 反熵冷凝中枢

<BlockImage id = "antientropy_condensation_center" scale = "4"/>

* 该机器每次工作前都会消耗凛冰粉, 消耗数量参考公式:

> <Latex math = "凛冰粉消耗量 = \frac{5 * (\frac{对应配方并行}{2^{19}} + 51 * ln对应配方并行)}{机器电压等级 - 9}" />

* 可以手持 <ItemLink id="kubejs:create_ultimate_battery" /> , 右键塞入一个到机器中, 获得额外的加成：
* 耗时倍数: 0.7; 耗能倍数: 0.5
* **注意**: 若将主机拆除后加成效果会重置且不会返还创造电池

