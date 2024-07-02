package net.countercraft.movecraft.movecraftoverheat.config;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    public static double HeatPerTNT = 1.0;
    public static double HeatPerFireball = 0.05;
    public static double HeatPerGunShot = 30;
    public static int HeatCheckInterval = 1000;
    public static int DisasterCheckInterval = 10000;

    public static boolean DebugMode;
    public static Map<Material, Double> RadiatorBlocks = new HashMap<>();
    public static Map<Material, Double> HeatSinkBlocks = new HashMap<>();

    public static boolean SilenceOverheatedCrafts = false;
    public static double SilenceHeatThreshold = 2.0;
}
