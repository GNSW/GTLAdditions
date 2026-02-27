---
navigation:
  title: Taixu Turbid Array
  icon: taixu_turbid_array
  parent: controller/multiblock_controller.md
  position: 10
item_ids:
  - taixu_turbid_array
---

# Taixu Turbid Array

<BlockImage id = "taixu_turbid_array" scale = "8"/>

> You can open the GUI of the main controller and put corresponding Nano Swarms or Creative Energy Hatch in the item slot at the bottom right corner of the GUI \
> When placing a **Creative Energy Hatch**, placing one will grant 3^16 parallel and 100% success rate bonus (~~placing multiple has the same effect~~) \
> When placing corresponding Nano Swarms, you get additional success rate bonus, with the bonus value being the number of Nano Swarms placed × base bonus \
> You can place up to 64 Nano Swarms or Creative Energy Hatches \
> When placing **Ender Nano Swarm**, the base bonus is 0.01 \
> When placing **Dragon Nano Swarm**, the base bonus is 0.05 \
> When placing **Spacetime Nano Swarm**, the base bonus is 0.1 \
> When placing **Eternal Nano Swarm**, the base bonus is 0.2

* This machine structure can only have one Laser Hatch
* When the machine voltage tier is greater than or equal to <Color color="#FFFF00">**UXV**</Color>, **UU Amplifier** output is unlocked
* When the machine voltage tier is greater than or equal to <Color color="#FF0000">**MAX**</Color>, **UU Matter** output is unlocked
* ~~When the machine voltage tier is not reached, there will be no additional UU Amplifier or UU Matter output~~
* The machine has a fixed processing time of 5 seconds, with fixed energy consumption of 524288 times the power corresponding to the current machine voltage tier

> **~~Lots of formulas warning~~**

* Bonus α calculation formula per Stellar Thermal Container level:
> <Latex math = "\alpha = 8 * (2^{Stellar Containment Tier} - 1) * \sqrt{Voltage Tier + 1}" />

* Bonus β calculation formula per Coil level:
> <Latex math = "\beta = 3.8 * 1.3^{Coil Level} * (\frac{Coil Temperature}{36000})^{0.7}" />

* UU Amplifier success probability calculation formula:
> <Latex math = "1/(1 + e^{-0.1 * (\frac{\alpha}{50} + \frac{\beta}{100} + \frac{height}{9})})" />

* UU Amplifier base output calculation formula:
> <Latex math = "4096 * (1 - e^{-0.015 * \alpha * \frac{height}{16} + \beta * \ln{(Voltage Tier + 2)}})" />

* UU Matter success probability calculation formula:
> <Latex math = "1/(1 - e^{-0.02 * (\frac{\alpha + \beta}{20} + \sqrt[3]{height} * \frac{Voltage Tier}{7})})" />

* UU Matter base output calculation formula:
> <Latex math = "2250 * \tanh{(\sqrt{\alpha * \beta} * \frac{(height + Voltage Tier) * 0.06}{200})}" />

* Machine parallel calculation formula:
> <Latex math = "4096 * 1.621^{\min(\frac{Coil Temperature}{6400},12)}" />

* **Note**: The height in the formulas refers to the height of repeated layers in the structure, similar to the height of a Distillation Tower or Neutron Activator

