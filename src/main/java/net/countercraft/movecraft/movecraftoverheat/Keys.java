package net.countercraft.movecraft.movecraftoverheat;

import org.bukkit.NamespacedKey;

public class Keys {
    public static final NamespacedKey BASE_HEAT_CAPACITY = build("base_heat_capacity");
    public static final NamespacedKey HEAT_CAPACITY_PER_BLOCK = build("heat_capacity_per_block");
    public static final NamespacedKey BASE_HEAT_DISSIPATION = build("base_heat_dissipation");
    public static final NamespacedKey HEAT_DISSIPATION_PER_BLOCK = build("heat_dissipation_per_block");
    public static final NamespacedKey TNT_HEAT_MULTIPLIER = build("tnt_heat_multiplier");
    public static final NamespacedKey FIREBALL_HEAT_MULTIPLIER = build("fireball_heat_multiplier");

    private static NamespacedKey build (String key) {return new NamespacedKey("movecraftOverheat", key);}
}
