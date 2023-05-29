package net.countercraft.movecraft.movecraftoverheat.disaster;

import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;

public class SurfaceExplosionType implements DisasterType {

    private double heatThreshold = 1.5;
    private double randomChance = 0.3;
    private double randomChancePowerFactor = 0.001;

    @Override
    public Disaster createNew(CraftHeat heat) {
        return new SurfaceExplosionDisaster(heat);
    }

    @Override
    public void setHeatThreshold(double threshold) {
        heatThreshold = threshold;
    }

    @Override
    public void setBaseRandomChance(double chance) {
        randomChance = chance;
    }

    @Override
    public void setRandomChancePowerFactor(double factor) {randomChancePowerFactor = factor;}

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

    @Override
    public double getRandomChancePowerFactor() { return randomChancePowerFactor; }
}
