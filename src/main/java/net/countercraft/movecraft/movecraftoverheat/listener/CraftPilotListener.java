package net.countercraft.movecraft.movecraftoverheat.listener;

import net.countercraft.movecraft.events.CraftDetectEvent;
import net.countercraft.movecraft.movecraftoverheat.Keys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CraftPilotListener implements Listener {
    @EventHandler
    public void onCraftPilot (CraftDetectEvent event) {
        //TODO: Hack, waiting for proper API support
        try {
            event.getCraft().getType().getIntProperty(Keys.BASE_HEAT_CAPACITY);
        } catch (Exception e) {
            return;
        }


    }
}
