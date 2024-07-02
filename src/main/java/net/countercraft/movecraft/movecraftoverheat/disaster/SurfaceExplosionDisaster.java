package net.countercraft.movecraft.movecraftoverheat.disaster;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;
import net.countercraft.movecraft.util.ChatUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class SurfaceExplosionDisaster implements Disaster {
    private final CraftHeat craftHeat;
    private final Craft craft;
    public static double HEAT_THRESHOLD = 1.5;
    public static double RANDOM_CHANCE = 0.3;

    public SurfaceExplosionDisaster (CraftHeat heat) {
        craftHeat = heat;
        craft = heat.getCraft();
    }

    @Override
    public void trigger () {
        int maxBombs = Math.max(Math.round(craft.getOrigBlockCount() / 1000f), 3);
        float bombChance = (maxBombs*1f)/(craft.getHitBox().getXLength() * craft.getHitBox().getZLength());
        int currentBombs = 0;
        for (MovecraftLocation location : craft.getHitBox()) {
            if (currentBombs >= maxBombs) break;
            if (location.getY() < craft.getHitBox().getMidPoint().getY() | Math.random() > bombChance) continue;
            if (craft.getWorld().getBlockAt(location.getX(), location.getY(), location.getZ()).getType().equals(Material.AIR)) continue;
            boolean success = true;
            for (int i = 1; i <= 4; i++) {
                Material type = craft.getWorld().getBlockAt(location.getX(), location.getY()+i, location.getZ()).getType();
                if (type != Material.AIR) {
                    success = false;
                    break;
                }
            }
            if (!success) continue;
            TNTPrimed bomb = (TNTPrimed)craft.getWorld().spawnEntity(location.toBukkit(craft.getWorld()), EntityType.PRIMED_TNT);
            bomb.setFuseTicks(0);
            bomb.setIsIncendiary(true);
            currentBombs++;
        }
        if (currentBombs > 0) {
            craft.getAudience().playSound(Sound.sound(Key.key("entity.wither.shoot"), Sound.Source.BLOCK, 9.0f, 3.0f));
            if (craft instanceof PlayerCraft) {
                ((PlayerCraft) craft).getPilot().sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + ChatColor.RED+ "Your craft has overheated and suffered an explosion!");
            }
        }
    }

    @Override
    public boolean testConditions () {
        return true;
    }


    @Override
    public CraftHeat getCraftHeat() {
        return craftHeat;
    }

    @Override
    public Craft getCraft() {
        return craft;
    }
}
