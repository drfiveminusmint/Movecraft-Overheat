package net.countercraft.movecraft.movecraftoverheat;

import net.countercraft.movecraft.Movecraft;
import net.countercraft.movecraft.combat.MovecraftCombat;
import org.bukkit.plugin.java.JavaPlugin;

public final class MovecraftOverheat extends JavaPlugin {
    public Movecraft movecraft;
    public MovecraftCombat movecraftCombat;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();
        this.movecraft = Movecraft.getInstance();
        this.movecraftCombat = MovecraftCombat.getInstance();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Movecraft getMovecraftPlugin() {
        return movecraft;
    }

    public MovecraftCombat getMovecraftCombatPlugin() {
        return movecraftCombat;
    }
}
