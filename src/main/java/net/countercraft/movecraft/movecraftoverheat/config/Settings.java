package net.countercraft.movecraft.movecraftoverheat.config;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    public static double heatPerTNT = 1.0;
    public static double heatPerFireball = 0.05;
    public static double heatPerGunShot = 30;

    public static boolean debug;
    public static Map<Material, Double> radiatorBlocks = new HashMap<>();
    public static Map<Material, Double> heatSinkBlocks = new HashMap<>();
}
