package eu.craftok.firespleef.manager.sb;

import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.manager.FGame;
import eu.craftok.firespleef.manager.player.FPlayer;
import eu.craftok.firespleef.manager.player.FPlayerManager;
import eu.craftok.firespleef.manager.task.GameTask;
import eu.craftok.utils.PlayerUtils;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class Scoreboard extends SidebarManager {

    @Override
    public void build(FPlayer player, SidebarEditor e) {
        FGame.STATE state = FMain.getInstance().getGame().getState();
        e.clear();
        PlayerUtils util = new PlayerUtils(player.getPlayer());
        e.setTitle("§c§lFIRESPLEEF");

        if (state == FGame.STATE.LOBBY) {
            e.add("");
            e.add("§c§LSERVEUR");
            e.add(" §fServeur §3» §b" + Bukkit.getServerName().replaceAll("Firespleef", "FS"));
            e.add(" §fEn jeu §3» §b" + FPlayerManager.getPlayingPlayers().size() + "/12");

            if (GameTask.timer != 60) {
                util.sendActionBar("§fDébut dans §b" + GameTask.timer + "§bs");
            } else {
                util.sendActionBar("§cManque de joueurs pour commencer...");
            }
            e.add("");
            e.add("§c§lPARTIE");
            e.add(" §fMode §3» §bSolo");
            e.add(" §fMap §3» §b" + FMain.getInstance().getGame().getMap());
            e.add("");
            e.add("§b[§fcraftok.fr§c]");
        } else if (state == FGame.STATE.GAME) {
            e.add("");
            e.add("§c§lPARTIE");
            e.add(" §fTemps de jeu §3» §b" + new SimpleDateFormat("mm:ss").format(GameTask.timer * 1000));
            e.add(" §fJoueurs restants §3» §b" + FPlayerManager.getPlayingFPlayers().size());
            e.add("");
            e.add(" §fDouble Jump §3» §b" + player.getDoublejump() + "/5");
            e.add("");
            e.add("§b[§fcraftok.fr§c]");
        } else {
            e.add("");
            if (player.isPlaying()){
                e.add(" §aVous avez gagné !");
            }else{
                e.add(" §cVous avez perdu !");
            }
            e.add("");
            e.add("§b[§fcraftok.fr§c]");
        }
    }


    public void uptadeAllTime() {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(FMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (FPlayerManager.getAllPlayers().size() != 0) {
                    FMain.getInstance().getScoreboard().update();
                }
            }
        }, 0L, 5L);
    }


}
