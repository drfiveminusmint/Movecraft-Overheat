package net.countercraft.movecraft.movecraftoverheat.listener;

import net.countercraft.movecraft.combat.features.tracking.events.CraftFireWeaponEvent;
import net.countercraft.movecraft.combat.features.tracking.types.Fireball;
import net.countercraft.movecraft.combat.features.tracking.types.TNTCannon;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.movecraftoverheat.Keys;
import net.countercraft.movecraft.movecraftoverheat.MovecraftOverheat;
import net.countercraft.movecraft.movecraftoverheat.config.Settings;
import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WeaponFireListener implements Listener {

    @EventHandler
    public void onFire (CraftFireWeaponEvent event) {
        Craft craft = event.getCraft();
        CraftHeat craftHeat = MovecraftOverheat.getInstance().getHeatManager().getHeat(craft);
        if (craftHeat == null) {
            return;
        }
        if (event.getWeaponType() instanceof Fireball) {
            double multiplier;
            try {
                multiplier = craft.getType().getDoubleProperty(Keys.FIREBALL_HEAT_MULTIPLIER);
            } catch (IllegalStateException e) {
                multiplier = 1.0;
            }
            craftHeat.addHeat(Settings.HeatPerFireball *multiplier);
        } else if (event.getWeaponType() instanceof TNTCannon) {
            double multiplier;
            try {
                multiplier = craft.getType().getDoubleProperty(Keys.TNT_HEAT_MULTIPLIER);
            } catch (IllegalStateException e) {
                multiplier = 1.0;
            }
            craftHeat.addHeat(Settings.HeatPerTNT*multiplier);
        }
    }
}
