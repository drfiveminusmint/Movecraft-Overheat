package net.countercraft.movecraft.movecraftoverheat.tracking;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.movecraftoverheat.Keys;
import net.countercraft.movecraft.movecraftoverheat.config.Settings;
import org.bukkit.Material;

public class CraftHeat {
    private Craft craft;
    private double heatCapacity;
    private double heat;
    private double dissipation;

    public void recalculate () {
        double newCapacity = craft.getType().getDoubleProperty(Keys.BASE_HEAT_CAPACITY);
        double newDissipation = craft.getType().getDoubleProperty(Keys.BASE_HEAT_DISSIPATION);
        double cPerBlock = craft.getType().getDoubleProperty(Keys.HEAT_CAPACITY_PER_BLOCK);
        double dPerBlock = craft.getType().getDoubleProperty(Keys.HEAT_DISSIPATION_PER_BLOCK);

        for (MovecraftLocation location : craft.getHitBox()) {
            Material type = craft.getWorld().getBlockAt(location.getX(), location.getY(), location.getZ()).getType();
            if (type == Material.AIR || type == Material.CAVE_AIR || type == Material.FIRE) {
                continue;
            }
            if (Settings.heatSinkBlocks.containsKey(type)) {
                newCapacity += Settings.heatSinkBlocks.get(type) * cPerBlock;
            } else {
                newCapacity += cPerBlock;
            }
            if (Settings.radiatorBlocks.containsKey(type)) {
                newDissipation += Settings.radiatorBlocks.get(type) * dPerBlock;
            } else {
                newDissipation += dPerBlock;
            }
        }

        if (newCapacity <= 1.0) {
            newCapacity = 1.0;
        }

        heat *= newCapacity / heatCapacity;
        heatCapacity = newCapacity;
        dissipation = newDissipation;
    }

    public void processDissipation () {
        if (heat > 0) {
            heat -= dissipation;
        }
        if (heat < 0) {
            heat = 0;
        }
    }
}
