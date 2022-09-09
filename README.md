# movecraft-overheat
A plugin that adds heat, cooling, and overheating mechanics to Movecraft.

This plugin requires Movecraft and Movecraft-Combat.

To make a craft type use heat mechanics, add the line ``UseHeat: true`` to the type file. From there, you can add several other modifiers:
``BaseHeatCapacity:`` The amount of heat capacity a craft of this type has initially, before any block modifiers. Default: 300
``CapacityPerBlock:`` The amount of heat capacity a craft gains per block of mass. This may be modified by HeatSinkBlocks. Default: 0.1
``BaseHeatDissipation:`` The amount of heat dissipation per second a craft of this type has initially. Default: 5.0
``DissipationPerBlock:`` Dissipation per second gained per block of mass. This is modified by RadiatorBlocks. Default: 0.005
``TNTHeatMultiplier:`` The multiplier on TNT-based heat for this craft type. Default: 1.0
``FireballHeatMultiplier`` Same as above, but for fireballs. Default: 1.0
