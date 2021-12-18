package eu.craftok.firespleef.manager.player.stats;

import eu.craftok.core.common.user.User;
import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.manager.FGame;
import eu.craftok.firespleef.manager.player.FPlayer;
import eu.craftok.utils.TimeUtils;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class FStats {

    private final FPlayer player;
    public FStats(FPlayer player){
        this.player = player;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer().getPlayer() == null) {
                    cancel();
                    return;
                }
                if (FMain.getInstance().getGame().getState() == FGame.STATE.GAME) {
                    if (getPlayer().isPlaying()){
                        User user = FMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
                        user.setStat("firespleef.playingtime", Integer.parseInt(user.getStat("firespleef.playingtime", 0)) + 1);
                    }
                }

            }
        }.runTaskTimerAsynchronously(FMain.getInstance(), 20, 20);
    }

    public FPlayer getPlayer() {
        return player;
    }

    public int getDeath(){
        User user = FMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return Integer.parseInt(user.getStat("firespleef.deaths", 0));
    }

    public void addDeath(){
        User user = FMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        user.setStat("firespleef.deaths", Integer.parseInt(user.getStat("firespleef.deaths", 0)) + 1);
    }

    public int getPlaying() {
        User user = FMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return Integer.parseInt(user.getStat("firespleef.playing", 0));
    }

    public void addPlaying(){
        User user = FMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        user.setStat("firespleef.playing", Integer.parseInt(user.getStat("firespleef.playing", 0)) + 1);
    }

    public String getPlayingtime() {
        User user = FMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return TimeUtils.getDurationBreakdown(Integer.parseInt(user.getStat("firespleef.playingtime", 0)) * 1000L);
    }

    public void addWin(){
        User user = FMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        user.setStat("firespleef.wins", Integer.parseInt(user.getStat("firespleef.wins", 0)) + 1);
    }

    public int getWins() {
        User user = FMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return Integer.parseInt(user.getStat("firespleef.wins", 0));
    }
}
