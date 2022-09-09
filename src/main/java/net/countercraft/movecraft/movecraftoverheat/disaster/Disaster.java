package net.countercraft.movecraft.movecraftoverheat.disaster;

import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;

public interface Disaster {

    double HEAT_THRESHOLD = 0;

    double RANDOM_CHANCE = 0;

    String NAME = "DEFAULT";

    void trigger();

    boolean testConditions();

    CraftHeat getCraftHeat ();

    Craft getCraft ();
}
