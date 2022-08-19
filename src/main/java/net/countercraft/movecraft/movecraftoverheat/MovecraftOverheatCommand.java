package net.countercraft.movecraft.movecraftoverheat;

import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.type.CraftType;
import net.countercraft.movecraft.movecraftoverheat.tracking.CraftHeat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MovecraftOverheatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("movecraftoverheat")) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("Please supply an argument. Valid arguments include: 'check'");
            return true;
        }

        if (args[0].equalsIgnoreCase("check")) {
            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You must be a player to use the command this way. Try /movecraftoverheat check <player>");
                    return true;
                }
                return checkCommand(sender, (Player) sender);
            } else {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage("Player '" + args[1] + "' not found.");
                    return true;
                }
                return checkCommand(sender, player);
            }
        }
        sender.sendMessage("Invalid argument: '" + args[0] + "'. Valid arguments include: 'check'");
        return true;
    }

    private boolean checkCommand (CommandSender sender, Player player) {
        Craft c = CraftManager.getInstance().getCraftByPlayer(player);
        if (c == null) {
            sender.sendMessage("Player '" + player.getDisplayName() + "' is not piloting a craft.");
            return true;
        }
        CraftHeat craftHeat = MovecraftOverheat.getInstance().getHeatManager().getHeat(c);
        if (craftHeat == null) {
            sender.sendMessage("This craft does not have a CraftHeat associated with it. Perhaps it does not use the Heat system?");
            return true;
        }
        sender.sendMessage("Checking craft of type " + c.getType().getStringProperty(CraftType.NAME) + " commanded by " + player.getDisplayName() + ":");
        sender.sendMessage("Current Heat: " + craftHeat.getHeat());
        sender.sendMessage("Capacity: " + craftHeat.getHeatCapacity());
        sender.sendMessage("Dissipation /second:" + craftHeat.getDissipation());
        return true;
    }
}
