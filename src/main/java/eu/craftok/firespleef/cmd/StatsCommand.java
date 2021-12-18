package eu.craftok.firespleef.cmd;

import eu.craftok.firespleef.manager.player.FPlayerManager;
import eu.craftok.firespleef.manager.player.stats.FStats;
import eu.craftok.firespleef.manager.ui.StatsUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class StatsCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.performCommand("stats " + player.getName());
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§c§lCRAFTOK §8§l» §cLe joueur est hors ligne !");
                    return false;
                }
                openStats(player, target);
            } else {
                player.performCommand("stats " + args[0]);
            }
        }
        return false;
    }

    private void openStats(Player sender, Player target) {
        FStats stats = FPlayerManager.getFPlayerByPlayer(target).getStats();
        new StatsUI(sender, stats).openMenu();
    }
}
