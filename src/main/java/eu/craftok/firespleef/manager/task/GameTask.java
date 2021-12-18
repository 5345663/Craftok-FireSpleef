package eu.craftok.firespleef.manager.task;

import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.manager.FGame;
import eu.craftok.firespleef.manager.player.FPlayerManager;
import eu.craftok.utils.PlayerUtils;
import eu.craftok.utils.firework.FireworkBuilder;
import eu.craftok.utils.firework.FireworkUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class GameTask extends BukkitRunnable {

    public static int timer = 60;
    public static List<Location> fireworkwin = new ArrayList<>();
    @Override
    public void run() {
        FGame.STATE state = FMain.getInstance().getGame().getState();
        PlayerUtils util = new PlayerUtils(FPlayerManager.getAllPlayers());
        if (state != FGame.STATE.LOBBY) {
            if (FPlayerManager.getPlayingFPlayers().size() == 0) {
                Bukkit.getServer().shutdown();
            }
        }
        if (state == FGame.STATE.LOBBY){
            if (timer == 30 || timer == 15 || timer == 10 || timer == 5 || timer == 4 || timer == 3 || timer == 2) {
                if (timer == 5){
                    util.sendTitle(5, 20, 5, "§a❺", "");
                }
                if (timer == 4){
                    util.sendTitle(5, 20, 5, "§e➍", "");
                }
                if (timer == 3){
                    util.sendTitle(5, 20, 5, "§e➌", "");
                }
                if (timer == 2){
                    util.sendTitle(5, 20, 5, "§6➋", "");
                }
                util.sendMessage("§c§lCRAFTOK §8§l» §7La partie va commencer dans §c" + timer + "§7 secondes");
                util.sendSound(Sound.NOTE_PIANO, 2f);
            }
            if (timer == 1) {
                util.sendMessage("§c§lCRAFTOK §8§l» §7La partie va commencer dans §c" + timer + "§7 seconde");
                util.sendTitle(5, 20, 5, "§c➊", "");
                util.sendSound(Sound.NOTE_PIANO, 2f);
            }
            if (timer == 0) {
                util.sendMessage("§7§m-------------------------------");
                util.sendMessage("§fBienvenue en §cFire Spleef §f!");
                util.sendMessage("§fLe but du jeu est simple : ");
                util.sendMessage("§fLancer vos boules de feu");
                util.sendMessage("§fet soyez le dernier en vie");
                util.sendMessage("§7§m-------------------------------");
                util.sendSound(Sound.ORB_PICKUP, 2f);
                FMain.getInstance().getGame().startGame();
                timer = 10 * 60;
                return;
            }
            if (FPlayerManager.getPlayingFPlayers().size() < FMain.getInstance().getGame().getMinplayers()) {
                cancel();
                timer = 60;
                return;
            }
        }else if (state == FGame.STATE.GAME){
            if (timer == 5 * 60){
                util.sendMessage("§c§lCRAFTOK §8§l» §7Debut du DeathMatch");
                FMain.getInstance().getGame().getDeathmatch().destroyY();
            }
            if (timer == 0){
                FMain.getInstance().getGame().setWin(true);
            }
        }else{
            fireworkwin.clear();

            FPlayerManager.getPlayingFPlayers().forEach(b -> {
                fireworkwin.add(b.getPlayer().getLocation());
            });

            fireworkwin.forEach(f -> FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), f));
            if (timer == 0) {
                FPlayerManager.getAllPlayers().forEach(jp -> {
                    jp.kickPlayer(null);
                });
                Bukkit.getServer().shutdown();
            }
        }
        timer--;
    }
}
