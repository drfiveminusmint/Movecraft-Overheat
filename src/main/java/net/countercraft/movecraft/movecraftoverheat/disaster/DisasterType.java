package net.countercraft.movecraft.movecraftoverheat.disaster;

import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;

public interface DisasterType {

    String disasterName = "DEFAULT";

    Disaster createNew (CraftHeat heat);

    void setHeatThreshold (double threshold);

    void setBaseRandomChance(double chance);

    void setRandomChancePowerFactor (double factor);

    double getHeatThreshold ();

    double getRandomChance ();

    String getDisasterName();

    double getRandomChancePowerFactor ();

}
