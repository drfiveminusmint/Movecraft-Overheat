package net.countercraft.movecraft.movecraftoverheat.disaster;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;
import net.countercraft.movecraft.util.ChatUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockIgniteEvent;

public class SurfaceFireDisaster implements Disaster {
    private final CraftHeat craftHeat;
    private final Craft craft;
    public static double HEAT_THRESHOLD = 1.0;
    public static double RANDOM_CHANCE = 0.3;

    public SurfaceFireDisaster (CraftHeat heat) {
        craftHeat = heat;
        craft = heat.getCraft();
    }

    @Override
    public void trigger () {
        int maxFires = Math.max(Math.round(craft.getOrigBlockCount() / 100f), 5);
        float fireChance = (maxFires*1f) / (craft.getHitBox().getXLength()*craft.getHitBox().getZLength());
        int currentFires = 0;
        for (MovecraftLocation location : craft.getHitBox()) {
            if (currentFires >= maxFires) break;
            if (location.getY() < craft.getHitBox().getMidPoint().getY() | Math.random() > fireChance) continue;
            boolean success = true;
            for (int i = 1; i <= 4; i++) {
                Material type = craft.getWorld().getBlockAt(location.getX(), location.getY()+i, location.getZ()).getType();
                if (type != Material.AIR) {
                    success = false;
                    break;
                }
            }
            if (!success) continue;
            Block block = craft.getWorld().getBlockAt(location.getX(), location.getY()+1, location.getZ());
            BlockIgniteEvent event = new BlockIgniteEvent(block, BlockIgniteEvent.IgniteCause.SPREAD,(Entity)null);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) continue;
            block.setType(Material.FIRE);
            currentFires++;
        }
        if (currentFires > 0) {
            craft.getAudience().playSound(Sound.sound(Key.key("item.firecharge.use"), Sound.Source.BLOCK, 5.0f, 5.0f));
            if (craft instanceof PlayerCraft) {
                ((PlayerCraft) craft).getPilot().sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + ChatColor.RED+ " The heat of your craft has set it ablaze!");
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
