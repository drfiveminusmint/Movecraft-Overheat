package net.countercraft.movecraft.movecraftoverheat;

import net.countercraft.movecraft.combat.MovecraftCombat;
import net.countercraft.movecraft.movecraftoverheat.config.Settings;
import net.countercraft.movecraft.movecraftoverheat.listener.CraftPilotListener;
import net.countercraft.movecraft.movecraftoverheat.listener.WeaponFireListener;
import net.countercraft.movecraft.movecraftoverheat.tracking.HeatManager;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.logging.Level;

public final class MovecraftOverheat extends JavaPlugin {
    private static MovecraftOverheat instance;
    private HeatManager manager;

    @Override
    public void onLoad () {
        Keys.register();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        if (!MovecraftCombat.getInstance().getConfig().getBoolean("EnableTNTTracking")) {
            getLogger().log(Level.SEVERE, "[ERROR] Movecraft-Overheat requires TNTTracking to be enabled in Movecraft-Combat. Disabling...");
            this.setEnabled(false);
            return;
        }
        if (!MovecraftCombat.getInstance().getConfig().getBoolean("EnableFireballTracking")) {
            getLogger().log(Level.SEVERE, "[ERROR] Movecraft-Overheat requires FireballTracking to be enabled in Movecraft-Combat. Disabling...");
            this.setEnabled(false);
            return;
        }

        this.manager = new HeatManager();

        getCommand("movecraftoverheat").setExecutor(new MovecraftOverheatCommand());

        getServer().getPluginManager().registerEvents(new CraftPilotListener(), this);
        getServer().getPluginManager().registerEvents(new WeaponFireListener(), this);

        Settings.HeatPerTNT = getConfig().getDouble("HeatPerTNT", 1.0);
        Settings.HeatPerFireball = getConfig().getDouble("HeatPerFireball", 0.05);
        Settings.HeatPerGunShot = getConfig().getDouble("HeatPerGunshot", 30);
        Settings.DebugMode = getConfig().getBoolean("DebugMode", false);
        if (getConfig().contains("heatSinkBlocks")) {
            Map<String,Object> tempMap = getConfig().getConfigurationSection("heatSinkBlocks").getValues(false);
            for(String str : tempMap.keySet()) {
                Material type;
                try {
                    type = Material.getMaterial(str);
                }
                catch(NumberFormatException e) {
                    type = Material.getMaterial(str);
                }
                Settings.HeatSinkBlocks.put(type,(Double)tempMap.get(str));
            }
        }
        if (getConfig().contains("radiatorBlocks")) {
            Map<String,Object> tempMap = getConfig().getConfigurationSection("radiatorBlocks").getValues(false);
            for(String str : tempMap.keySet()) {
                Material type;
                try {
                    type = Material.getMaterial(str);
                }
                catch(NumberFormatException e) {
                    type = Material.getMaterial(str);
                }
                Settings.RadiatorBlocks.put(type,(Double)tempMap.get(str));
            }
        }

        if (Settings.RadiatorBlocks.isEmpty()) {
            getLogger().log(Level.SEVERE,"Radiator blocks empty!");
        }

        manager.runTaskTimer(this, 20, 4);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public HeatManager getHeatManager() {
        return manager;
    }

    public static MovecraftOverheat getInstance () {
        return instance;
    }
}
