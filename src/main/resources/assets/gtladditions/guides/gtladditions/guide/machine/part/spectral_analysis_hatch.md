---
navigation:
  title: Spectral Analysis Hatch
  icon: spectral_analysis_hatch
  parent: part/machine_part_index.md
  position: 10
item_ids:
  - gtladditions:spectral_analysis_hatch
---

# Spectral Analysis Hatch

~~Alias OP Hatch~~

<BlockImage id="spectral_analysis_hatch" scale="8" />

* Must be crafted in the <Color color="#00AAAA">**UV**</Color> tier
* The Spectral Analysis Hatch can only be installed on an Integrated Ore Processing or Advanced Integrated Ore Processing. It's an additional part hatch and only one can be installed in the structure
* Opening the hatch GUI shows three channels, each with three indicator lights
* The channel number for each band is randomly generated ~~(but follows certain patterns)~~
* The number of activated indicator lights after each channel represents the corresponding activation level, <Color color="#00FF00">green indicates active state</Color>, <Color color="#FF0000">red indicates inactive state</Color>
* Activation Level 1 (value range **±80**), Activation Level 2 (value range **±35**), Activation Level 3 (value range **±5**)
* You can adjust the channel by modifying the value or dragging the slider below the text box


* Channel 1: Thread Bonus, directly applied to multi-recipe processing threads
> **Integrated Ore Processing**: Level 0 (default bonus): 4 threads, Level 1: 6, Level 2: 8, Level 3: 10
>
> **Advanced Integrated Ore Processing**: Level 0 (default bonus): 72 threads, Level 1: 96, Level 2: 128, Level 3: 144
* Channel 2: Probability Bonus, all probability output voltage increase probabilities are **directly multiplied** by the channel level plus one
> For example: At Level 2, when outputting item A with base output probability of 30% and base voltage bonus probability of 5%, the base voltage bonus probability becomes (5 * (2 + 1)) = 15% after this band's bonus
* Channel 3: Independent Bonus, different bonuses for Integrated Ore Processing and Advanced Integrated Ore Processing, different bonuses when installed on different machine structures
> When installed on **Integrated Ore Processing**, get recipe time multiplier bonus (**direct multiplication**)
>
> When installed on **Advanced Integrated Ore Processing**, get fluid consumption multiplier bonus (**direct multiplication**)
>
> Bonus multiplier: Level 0 (default bonus): 0.8, Level 1: 0.65, Level 2: 0.5, Level 3: 0.4

* Additionally, when the input channel matches the actual channel (i.e., zero error), it's called Perfect Tuning
* When all three channels are perfectly tuned, the machine overclocking changes from lossy overclocking to lossless overclocking and can use Extreme Processing Mode for more efficient recipe processing, greatly improving machine processing efficiency
* ~~(All three channels must be perfectly tuned to get this bonus)~~

