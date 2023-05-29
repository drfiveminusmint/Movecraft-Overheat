package net.countercraft.movecraft.movecraftoverheat;

import net.countercraft.movecraft.combat.MovecraftCombat;
import net.countercraft.movecraft.movecraftoverheat.config.Settings;
import net.countercraft.movecraft.movecraftoverheat.disaster.DisasterType;
import net.countercraft.movecraft.movecraftoverheat.disaster.SurfaceExplosionType;
import net.countercraft.movecraft.movecraftoverheat.disaster.SurfaceFireType;
import net.countercraft.movecraft.movecraftoverheat.listener.CraftPilotListener;
import net.countercraft.movecraft.movecraftoverheat.listener.WeaponFireListener;
import net.countercraft.movecraft.movecraftoverheat.tracking.HeatManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;

public final class MovecraftOverheat extends JavaPlugin {
    private static MovecraftOverheat instance;
    private HeatManager manager;
    private static HashSet<DisasterType> disasterTypes = new HashSet<>();

    @Override
    public void onLoad () {
        addDisasterType(new SurfaceExplosionType());
        addDisasterType(new SurfaceFireType());
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
        Settings.HeatCheckInterval = getConfig().getInt("HeatCheckInterval", 1000);
        Settings.DisasterCheckInterval = getConfig().getInt("DisasterCheckInterval", 10000);
        if (getConfig().contains("HeatSinkBlocks")) {
            Map<String,Object> tempMap = getConfig().getConfigurationSection("HeatSinkBlocks").getValues(false);
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
        if (getConfig().contains("RadiatorBlocks")) {
            Map<String,Object> tempMap = getConfig().getConfigurationSection("RadiatorBlocks").getValues(false);
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

        if (Settings.HeatSinkBlocks.isEmpty()) {
            getLogger().log(Level.WARNING, "Heat Sink blocks empty!");
        }

        if (Settings.RadiatorBlocks.isEmpty()) {
            getLogger().log(Level.WARNING,"Radiator blocks empty!");
        }

        if (getConfig().contains("Disasters")) {
            Map<String,Object> tempMap = getConfig().getConfigurationSection("Disasters").getValues(false);
            for (String str : tempMap.keySet()) {
                for (DisasterType d : disasterTypes) {
                    if (str.equalsIgnoreCase(d.getDisasterName())) {
                        Map<String, Object> disasterData = ((ConfigurationSection)tempMap.get(str)).getValues(false);
                        d.setHeatThreshold((Double)disasterData.getOrDefault("HeatThreshold", 0d));
                        d.setBaseRandomChance((Double)disasterData.getOrDefault("RandomChance", 0d));
                        d.setRandomChancePowerFactor((Double)disasterData.getOrDefault("RandomChancePowerFactor", 0.001));
                    }
                }
            }
        }

        manager.runTaskTimer(this, 20, 4);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void addDisasterType(DisasterType d) {
        disasterTypes.add(d);
    }
    public HeatManager getHeatManager() {
        return manager;
    }

    public static MovecraftOverheat getInstance () {
        return instance;
    }

    public static HashSet<DisasterType> getDisasterTypes () {
        return disasterTypes;
    }
}
