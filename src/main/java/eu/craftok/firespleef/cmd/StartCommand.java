package eu.craftok.firespleef.cmd;

import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.manager.FGame;
import eu.craftok.firespleef.manager.player.FPlayerManager;
import eu.craftok.firespleef.manager.task.GameTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class StartCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()){
                if (FMain.getInstance().getGame().getState() == FGame.STATE.LOBBY) {
                    if (FPlayerManager.getPlayingPlayers().size() > 1) {
                        player.sendMessage("§c§lCRAFTOK §8§l» §7Vous venez de lancer la partie §a" + Bukkit.getServerName() + " §7!");
                        FMain.getInstance().getGame().setMinplayers(2);
                        if (GameTask.timer == 60) {
                            new GameTask().runTaskTimer(FMain.getInstance(), 0, 20);
                        }
                        GameTask.timer = 3;
                    } else {
                        player.sendMessage("§c§lCRAFTOK §8§l» §cVous ne pouvez pas lancer la partie car il faut 2 joueurs minimum !");
                    }
                }
            }else{
                player.sendMessage("§c§lCRAFTOK §8§l» §cVous n'avez pas l'autorisation de faire ceci !");
            }
        }
        return false;
    }
}
