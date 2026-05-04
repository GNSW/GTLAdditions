---
navigation:
  title: Antientropy Condensation
  icon: antientropy_condensation_center
  parent: controller/multiblock_controller.md
  position: 10
item_ids:
  - antientropy_condensation_center
---

# Antientropy Condensation

<BlockImage id = "antientropy_condensation_center" scale = "4"/>

* This machine consumes Cryotheum Dust before each operation. The consumption amount is calculated by the formula:

> <Latex math = "Cryotheum Dust Consumption = \frac{5 * (\frac{Recipe Parallel}{2^{19}} + 51 * \ln{Recipe Parallel})}{Voltage Tier - 9}" />

* You can hold <ItemLink id="kubejs:create_ultimate_battery" /> and right-click to insert one into the machine for additional bonus:
* Time multiplier: 0.7; Energy multiplier: 0.5
* **Note**: The bonus effect will be reset if the machine is removed and the Creative Ultimate Battery will not be returned
