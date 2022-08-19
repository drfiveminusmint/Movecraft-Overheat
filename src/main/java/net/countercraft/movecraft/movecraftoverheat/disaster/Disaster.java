package net.countercraft.movecraft.movecraftoverheat.disaster;

import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;

public interface Disaster {
    void trigger();

    boolean testConditions();

    CraftHeat getCraftHeat ();

    Craft getCraft ();
}
