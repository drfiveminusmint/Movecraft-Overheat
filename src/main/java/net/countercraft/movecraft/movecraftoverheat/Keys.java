package net.countercraft.movecraft.movecraftoverheat;

import net.countercraft.movecraft.craft.type.CraftType;
import net.countercraft.movecraft.craft.type.property.BooleanProperty;
import net.countercraft.movecraft.craft.type.property.DoubleProperty;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Keys {
    public static final NamespacedKey BASE_HEAT_CAPACITY = build("base_heat_capacity");
    public static final NamespacedKey HEAT_CAPACITY_PER_BLOCK = build("heat_capacity_per_block");
    public static final NamespacedKey BASE_HEAT_DISSIPATION = build("base_heat_dissipation");
    public static final NamespacedKey HEAT_DISSIPATION_PER_BLOCK = build("heat_dissipation_per_block");
    public static final NamespacedKey TNT_HEAT_MULTIPLIER = build("tnt_heat_multiplier");
    public static final NamespacedKey FIREBALL_HEAT_MULTIPLIER = build("fireball_heat_multiplier");
    public static final NamespacedKey USE_HEAT = build("use_heat");

    public static void register() {
        CraftType.registerProperty(new DoubleProperty("BaseHeatCapacity", BASE_HEAT_CAPACITY, craftType -> 300.0));
        CraftType.registerProperty(new DoubleProperty("CapacityPerBlock", HEAT_CAPACITY_PER_BLOCK, craftType -> 0.1));
        CraftType.registerProperty(new DoubleProperty("BaseHeatDissipation", BASE_HEAT_DISSIPATION, craftType -> 5.0));
        CraftType.registerProperty(new DoubleProperty("DissipationPerBlock", HEAT_DISSIPATION_PER_BLOCK, craftType -> 0.005));
        CraftType.registerProperty(new DoubleProperty("TNTHeatMultiplier", TNT_HEAT_MULTIPLIER, craftType -> 1.0));
        CraftType.registerProperty(new DoubleProperty("FireballHeatMultiplier", FIREBALL_HEAT_MULTIPLIER, craftType -> 1.0));
        CraftType.registerProperty(new BooleanProperty("UseHeat", USE_HEAT, craftType -> false));
    }

    @NotNull
    @Contract("_ -> new")
    private static NamespacedKey build(String key) {
        return new NamespacedKey("movecraft-overheat", key);
    }
}
