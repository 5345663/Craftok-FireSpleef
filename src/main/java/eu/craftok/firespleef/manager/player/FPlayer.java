package eu.craftok.firespleef.manager.player;

import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.manager.player.stats.FStats;
import eu.craftok.firespleef.manager.task.GameTask;
import eu.craftok.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

import java.util.UUID;

import static eu.craftok.firespleef.manager.FGame.STATE.*;

public class FPlayer {

    private final UUID uuid;
    private boolean playing;
    private FStats stats;
    private int doublejump;

    public FPlayer(UUID uuid){
        this.uuid = uuid;
        this.playing = false;
        this.stats = new FStats(this);
        this.doublejump = 5;
    }


    public UUID getUniqueId() {
        return uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public FStats getStats() {
        return stats;
    }

    public int getDoublejump() {
        return doublejump;
    }

    public void addDoubleJump(){
        doublejump++;
    }

    public void removeDoubleJump(){
        doublejump--;
    }

    public void login(PlayerJoinEvent event){
        FPlayerManager.players.add(this);
        FMain.getInstance().getScoreboard().join(this);
        PlayerUtils util = new PlayerUtils(event.getPlayer());
        util.sendMessage("§c§lCRAFTOK §8§l» §fMode de jeu en phase §cBETA§f, en cas de bug merci d'ouvrir un ticket sur discord ! Bon jeu !");

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        if (FMain.getInstance().getGame().getState() != LOBBY) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            playing = true;
            player.setAllowFlight(false);
            player.setGameMode(GameMode.ADVENTURE);
            if (GameTask.timer == 60) {
                GameTask.timer--;
                new GameTask().runTaskTimer(FMain.getInstance(), 0, 20);
            }
            if (FPlayerManager.getAllPlayers().size() == 10){
                GameTask.timer = 3;
            }
        }
        if (isPlaying())
            Bukkit.broadcastMessage("§c" + event.getPlayer().getName() + " §7vient de rejoindre la partie §a(" + FPlayerManager.getPlayingPlayers().size() + "/12)");

        player.setMaxHealth(20.0D);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(12.8F);
        player.setMaximumNoDamageTicks(20);
        player.setFireTicks(0);
        player.setFallDistance(0.0F);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setWalkSpeed(0.2F);
        player.getInventory().setHeldItemSlot(0);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.closeInventory();
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(FMain.getInstance().getGame().getSpawn());
            }
        }.runTaskLater(FMain.getInstance(), 2);
    }

    public void logout(PlayerQuitEvent event){
        FMain.getInstance().getScoreboard().leave(this);
        FPlayerManager.players.remove(this);
        event.setQuitMessage(null);
        if (isPlaying()){
            Bukkit.broadcastMessage("§c" + event.getPlayer().getName() + " §7vient de quitter la partie §c(" + FPlayerManager.getPlayingPlayers().size() + "/12)");
            if (FMain.getInstance().getGame().getState() == GAME)
              death();
        }
    }

    public void death(){
        if (FMain.getInstance().getGame().getState().equals(GAME) && isPlaying()) {
            PlayerUtils util = new PlayerUtils(FPlayerManager.getAllPlayers());
            stats.addDeath();
            getPlayer().teleport(FMain.getInstance().getGame().getSpawn());
            getPlayer().setGameMode(GameMode.SPECTATOR);
            playing = false;
            util.sendMessage("§c§lCRAFTOK §8§l» §c§l" + getPlayer().getName() + " §fvient d'être eliminé");
            util.sendSound(Sound.GHAST_SCREAM, 2f);
            if (FPlayerManager.getPlayingFPlayers().size() == 1) {
                FMain.getInstance().getGame().setWin(false);
            }
        }
    }

}
