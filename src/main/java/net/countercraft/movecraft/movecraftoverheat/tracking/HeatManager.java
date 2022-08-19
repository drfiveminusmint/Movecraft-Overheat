package net.countercraft.movecraft.movecraftoverheat.tracking;

import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.movecraftoverheat.MovecraftOverheat;
import net.countercraft.movecraft.movecraftoverheat.disaster.Disaster;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class HeatManager extends BukkitRunnable {
    private final HashMap<Craft, CraftHeat> heatTracking = new HashMap<>();
    private final Queue<Disaster> disasterQueue = new ConcurrentLinkedQueue();

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        for (CraftHeat heat : heatTracking.values()) {
            if (heat.getLastUpdate() + 1000 >= time) {
                continue;
            }
            heat.recalculate();
            heat.processDissipation();
            heat.setLastUpdate(time);
            if (heat.getLastDisaster() + 10000 <= time) {
                heat.checkDisasters();
            }
        }
        if (disasterQueue.isEmpty()) return;
        Disaster disaster = disasterQueue.poll();
        if (disaster.testConditions()) {
            disaster.trigger();
        }
    }

    public void registerCraft(Craft c) {
        if (heatTracking.get(c) != null) {
           return;
        }
        MovecraftOverheat.getInstance().getLogger().log(Level.ALL, "Registered a new craft!");
        heatTracking.putIfAbsent(c, new CraftHeat(c));
    }

    public void removeCraft(Craft c) {
        heatTracking.remove(c);
    }

    @Nullable
    public CraftHeat getHeat(Craft c) {
        return heatTracking.get(c);
    }

    public void addDisaster (Disaster disaster) {
        disasterQueue.add(disaster);
    }
}
