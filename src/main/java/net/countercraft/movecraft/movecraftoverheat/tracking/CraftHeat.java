package net.countercraft.movecraft.movecraftoverheat.tracking;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.craft.datatag.CraftDataTagContainer;
import net.countercraft.movecraft.craft.datatag.CraftDataTagKey;
import net.countercraft.movecraft.movecraftoverheat.Keys;
import net.countercraft.movecraft.movecraftoverheat.MovecraftOverheat;
import net.countercraft.movecraft.movecraftoverheat.config.Settings;
import net.countercraft.movecraft.movecraftoverheat.disaster.DisasterType;
import net.countercraft.movecraft.util.ChatUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.NotNull;

public class CraftHeat {
    public static final CraftDataTagKey<Double> HEAT_CAPACITY = CraftDataTagContainer.tryRegisterTagKey(new NamespacedKey("movecraft-overheat", "heat-capacity"), craft -> 0D);
    private final Craft craft;
    private double heat;
    private double dissipation;
    private long lastUpdate;
    private long lastDisaster;
    private boolean silenced;

    private boolean firedThisTick;
    private int explosionsThisTick;
    private final BossBar bossBar;

    public CraftHeat (@NotNull Craft c) {
        this.craft = c;
        this.heat = 0;
        this.bossBar = Bukkit.createBossBar("Heat", BarColor.GREEN, BarStyle.SEGMENTED_6);
        this.recalculate();
        if (craft instanceof PlayerCraft) {
            this.bossBar.addPlayer(((PlayerCraft) this.craft).getPilot());
        }
    }

    public void recalculate () {
        double newCapacity = craft.getType().getDoubleProperty(Keys.BASE_HEAT_CAPACITY);
        double newDissipation = craft.getType().getDoubleProperty(Keys.BASE_HEAT_DISSIPATION);
        double cPerBlock = craft.getType().getDoubleProperty(Keys.HEAT_CAPACITY_PER_BLOCK);
        double dPerBlock = craft.getType().getDoubleProperty(Keys.HEAT_DISSIPATION_PER_BLOCK);

        for (MovecraftLocation location : craft.getHitBox()) {
            Material type = craft.getWorld().getBlockAt(location.getX(), location.getY(), location.getZ()).getType();
            if (type == Material.AIR || type == Material.CAVE_AIR || type == Material.FIRE) {
                continue;
            }
            if (Settings.HeatSinkBlocks.containsKey(type)) {
                newCapacity += Settings.HeatSinkBlocks.get(type) * cPerBlock;
            } else {
                newCapacity += cPerBlock;
            }
            if (Settings.RadiatorBlocks.containsKey(type)) {
                newDissipation += Settings.RadiatorBlocks.get(type) * dPerBlock;
            } else {
                newDissipation += dPerBlock;
            }
        }

        if (newCapacity <= 1.0) {
            newCapacity = 1.0;
        }
        newCapacity = Math.round(newCapacity);

        if (Math.abs(heat) >= 0.01) {
            heat *= newCapacity / craft.getDataTag(HEAT_CAPACITY);
        }
        craft.setDataTag(HEAT_CAPACITY, newCapacity);
        dissipation = newDissipation;

        updateBossBar();
    }

    public void processDissipation () {
        if (heat > 0.0) {
            heat -= dissipation;
        }
        if (heat < 0.0) {
            heat = 0.0;
        }
    }

    public void checkDisasters () {
        // Update whether the craft is silenced
        if (silenced) {
            if (heat < craft.getDataTag(HEAT_CAPACITY) * Settings.SilenceHeatThreshold) {
                craft.getAudience().sendMessage(Component.text(ChatUtils.MOVECRAFT_COMMAND_PREFIX + "No longer silenced!"));
                silenced = false;
            }
        } else {
            if (Settings.SilenceOverheatedCrafts && heat > craft.getDataTag(HEAT_CAPACITY) * Settings.SilenceHeatThreshold) {
                craft.getAudience().sendMessage(Component.text(ChatUtils.MOVECRAFT_COMMAND_PREFIX + ChatColor.RED + "Silenced! Your guns are too hot to fire!"));
                craft.getAudience().playSound(Sound.sound(Key.key("entity.blaze.death"), Sound.Source.BLOCK, 2.0f, 1.0f));
                silenced = true;
            }
        }
        for (DisasterType type : MovecraftOverheat.getDisasterTypes()) {
            if (type.getHeatThreshold() * craft.getDataTag(HEAT_CAPACITY) > heat) continue;
            if ((1-type.getRandomChance()) * (Math.exp(-1 * type.getRandomChancePowerFactor() * ((heat/craft.getDataTag(HEAT_CAPACITY))-type.getHeatThreshold()))) > Math.random()) continue;
            MovecraftOverheat.getInstance().getHeatManager().addDisaster(type.createNew(this));
            lastDisaster = System.currentTimeMillis();
        }
    }

    public Craft getCraft () {
        return craft;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long l) {
        lastUpdate = l;
    }

    public double getHeat() {
        return heat;
    }

    public double getDissipation() {
        return dissipation;
    }

    public void addHeat (double heatToAdd) {
        heat += heatToAdd;
        updateBossBar();
    }

    public long getLastDisaster() {
        return lastDisaster;
    }

    public boolean isSilenced() {
        return silenced;
    }

    private void updateBossBar() {
        bossBar.setTitle("Heat: " + Math.round(heat*10)/10d + " / " + craft.getDataTag(HEAT_CAPACITY));
        if (heat >= craft.getDataTag(HEAT_CAPACITY)*1.5) {
            bossBar.setColor(BarColor.RED);
        } else if (heat >= craft.getDataTag(HEAT_CAPACITY)) {
            bossBar.setColor(BarColor.YELLOW);
        } else {
            bossBar.setColor(BarColor.GREEN);
        }
        bossBar.setProgress(Math.min(1.0, heat / craft.getDataTag(HEAT_CAPACITY)));
        bossBar.setVisible(heat != 0.0);
    }

    public void removeBossBar () {
        bossBar.removeAll();
    }

    public void reportExplosion() {
        if (++explosionsThisTick >= Settings.ExplosionsPerGunshot && !firedThisTick) {
            addHeat(Settings.HeatPerGunshot);
            firedThisTick = true;
        }
    }

    public void resetGunshotDetection() {
        firedThisTick = false;
        explosionsThisTick = 0;
    }
}
