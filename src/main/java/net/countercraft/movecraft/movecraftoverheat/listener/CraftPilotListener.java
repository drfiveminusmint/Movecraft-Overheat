package net.countercraft.movecraft.movecraftoverheat.listener;

import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.events.CraftDetectEvent;
import net.countercraft.movecraft.events.CraftReleaseEvent;
import net.countercraft.movecraft.events.CraftSinkEvent;
import net.countercraft.movecraft.movecraftoverheat.Keys;
import net.countercraft.movecraft.movecraftoverheat.MovecraftOverheat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class CraftPilotListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onCraftPilot(@NotNull CraftDetectEvent event) {
        if (!(event.getCraft() instanceof PlayerCraft) || !event.getCraft().getType().getBoolProperty(Keys.USE_HEAT))
            return;

        MovecraftOverheat.getInstance().getHeatManager().registerCraft(event.getCraft());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onCraftRelease(@NotNull CraftReleaseEvent event) {
        if(!(event.getCraft() instanceof PlayerCraft) || (MovecraftOverheat.getInstance().getHeatManager().getHeat(event.getCraft()) == null)) {
            return;
        }
        MovecraftOverheat.getInstance().getHeatManager().getHeat(event.getCraft()).removeBossBar();
        MovecraftOverheat.getInstance().getHeatManager().removeCraft(event.getCraft());
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftSink(@NotNull CraftSinkEvent event) {
        if(!(event.getCraft() instanceof PlayerCraft) || (MovecraftOverheat.getInstance().getHeatManager().getHeat(event.getCraft()) == null)) {
            return;
        }
        MovecraftOverheat.getInstance().getHeatManager().getHeat(event.getCraft()).removeBossBar();
        MovecraftOverheat.getInstance().getHeatManager().removeCraft(event.getCraft());
    }
}
