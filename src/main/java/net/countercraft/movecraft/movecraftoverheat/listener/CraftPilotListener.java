package net.countercraft.movecraft.movecraftoverheat.listener;

import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.events.CraftDetectEvent;
import net.countercraft.movecraft.events.CraftReleaseEvent;
import net.countercraft.movecraft.events.CraftSinkEvent;
import net.countercraft.movecraft.movecraftoverheat.MovecraftOverheat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CraftPilotListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onCraftPilot (CraftDetectEvent event) {
        if (!(event.getCraft() instanceof PlayerCraft) | event.isCancelled()) {
            return;
        }
        MovecraftOverheat.getInstance().getHeatManager().registerCraft(event.getCraft());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCraftRelease (CraftReleaseEvent event) {
        if(!(event.getCraft() instanceof PlayerCraft) | (event.isCancelled() | MovecraftOverheat.getInstance().getHeatManager().getHeat(event.getCraft())==null)) {
            return;
        }
        MovecraftOverheat.getInstance().getHeatManager().getHeat(event.getCraft()).removeBossBar();
        MovecraftOverheat.getInstance().getHeatManager().removeCraft(event.getCraft());
    }

    @EventHandler
    public void onCraftSink (CraftSinkEvent event) {
        if(!(event.getCraft() instanceof PlayerCraft) | (event.isCancelled() | MovecraftOverheat.getInstance().getHeatManager().getHeat(event.getCraft())==null)) {
            return;
        }
        MovecraftOverheat.getInstance().getHeatManager().getHeat(event.getCraft()).removeBossBar();
        MovecraftOverheat.getInstance().getHeatManager().removeCraft(event.getCraft());
    }
}
