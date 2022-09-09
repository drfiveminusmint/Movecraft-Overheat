package net.countercraft.movecraft.movecraftoverheat.disaster;

import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;

public class SurfaceExplosionType implements DisasterType {

    private double heatThreshold = 1.5;
    private double randomChance = 0.3;

    @Override
    public Disaster createNew(CraftHeat heat) {
        return new SurfaceExplosionDisaster(heat);
    }

    @Override
    public void setHeatThreshold(double threshold) {
        heatThreshold = threshold;
    }

    @Override
    public void setRandomChance(double chance) {
        randomChance = chance;
    }

    @Override
    public double getHeatThreshold() {
        return heatThreshold;
    }

    @Override
    public double getRandomChance() {
        return randomChance;
    }

    @Override
    public String getDisasterName() {
        return "SurfaceExplosion";
    }
}
