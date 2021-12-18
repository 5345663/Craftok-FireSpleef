package eu.craftok.firespleef.manager;

import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.cmd.CommandRegister;
import eu.craftok.firespleef.listener.EventRegister;
import eu.craftok.firespleef.manager.deathmatch.DeathMatch;
import eu.craftok.firespleef.manager.player.FPlayer;
import eu.craftok.firespleef.manager.player.FPlayerManager;
import eu.craftok.firespleef.manager.task.GameTask;
import eu.craftok.utils.CConfig;
import eu.craftok.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.bukkit.Material.WOOL;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class FGame {

    private CConfig configuration;
    private Location spawn;
    private int minplayers, maxplayers;
    private STATE state;
    private String map;
    private List<Location> spawnpoints = new ArrayList<>();
    private List<Location> bonuspoints = new ArrayList<>();
    private List<Block> bonuscache = new ArrayList<>();
    private DeathMatch deathmatch;

    public void setupGame(){
        EventRegister.registerEvent();
        CommandRegister.registerCommand();
        for (World world : Bukkit.getWorlds()) {
            world.setTime(0);
            world.setDifficulty(Difficulty.EASY);
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
        }
        this.configuration = new CConfig("configuration.yml", FMain.getInstance());
        this.configuration.addValue("spawn", "world:-7.5:72:23.5:180:3");
        this.configuration.addValue("minplayers", 4);
        this.configuration.addValue("maxplayers", 12);
        this.configuration.addValue("map.name", "Test");
        this.configuration.addValue("map.spawnpoints", Arrays.asList("world:-7.5:72:23.5:180:3", "world:2.5:73:1.5:180:3", "world:-6.5:73:-7.5:180:3", "world:8.5:72:22.5:180:3", "world:-26.5:71:17.5:180:3", "world:-25.5:72:3.5:180:3", "world:-14.5:71:9.5:180:3", "world:12.5:71:20.5:180:3", "world:-1.5:70:36.5:180:3", "world:-16.5:78:24.5:180:3", "world:-13.5:78:0.5:180:3", "world:5.5:80:4.5:180:3"));
        this.configuration.addValue("map.bonuspoints", Arrays.asList("world:-7.5:72:23.5:180:3", "world:2.5:73:1.5:180:3", "world:-6.5:73:-7.5:180:3", "world:8.5:72:22.5:180:3", "world:-26.5:71:17.5:180:3", "world:-25.5:72:3.5:180:3", "world:-14.5:71:9.5:180:3", "world:12.5:71:20.5:180:3", "world:-1.5:70:36.5:180:3", "world:-16.5:78:24.5:180:3", "world:-13.5:78:0.5:180:3", "world:5.5:80:4.5:180:3"));
        this.configuration.addValue("corner1", "world:-7.5:72:23.5:180:3");
        this.configuration.addValue("corner2", "world:-7.5:72:23.5:180:3");
        this.spawn = CConfig.getLocationString(this.configuration.getValue("spawn").toString());
        this.spawn.setYaw(Float.parseFloat(this.configuration.getValue("spawn").toString().split(":")[4]));
        this.spawn.setPitch(Float.parseFloat(this.configuration.getValue("spawn").toString().split(":")[5]));
        this.map = this.configuration.getValue("map.name").toString();
        for (String s : this.configuration.getConfig().getStringList("map.spawnpoints")) {
            Location location = CConfig.getLocationString(s);
            this.spawnpoints.add(location);
        }
        for (String s : this.configuration.getConfig().getStringList("map.bonuspoints")) {
            Location location = CConfig.getLocationString(s);
            this.bonuspoints.add(location);
            location.getChunk().load();
        }
        this.minplayers = ((int) this.configuration.getValue("minplayers"));
        this.maxplayers = ((int) this.configuration.getValue("maxplayers"));
        deathmatch = new DeathMatch(CConfig.getLocationString(this.configuration.getValue("corner1").toString()), CConfig.getLocationString(this.configuration.getValue("corner2").toString()));
        this.state = STATE.LOBBY;
    }

    public void startGame(){
        setState(STATE.GAME);
        teleportAll();
        spawnBonus();
    }

    public void teleportAll(){
        List<FPlayer> player = new ArrayList<>(FPlayerManager.getPlayingFPlayers());
        List<Location> spawn = new ArrayList<>(spawnpoints);
        player.forEach(p -> {
            if (spawn.isEmpty())
                spawn.addAll(spawnpoints);
            Location s = spawn.remove(0);
            p.getStats().addPlaying();
            p.getPlayer().teleport(s);
            p.getPlayer().setGameMode(GameMode.ADVENTURE);
            p.getPlayer().getInventory().setItem(0, new ItemStack(Material.FIREBALL));
        });
    }

    public void setWin(boolean equality) {
        if (state == STATE.GAME) {
            FPlayerManager.getAllPlayers().forEach(p -> p.setGameMode(GameMode.SPECTATOR));
            setState(STATE.FINISH);
            GameTask.timer = 15;
            FPlayerManager.getPlayingFPlayers().forEach(w -> {
                PlayerUtils utils = new PlayerUtils(w.getPlayer());
                if (equality){
                    utils.sendTitle(5, 20, 5, "§c§LPERDU", "§rVous avez fait egaliter");
                    w.setPlaying(false);
                }
                else {
                    utils.sendTitle(5, 20, 5, "§c§LVICTOIRE", "§rVous venez de gagner la partie");
                    w.getStats().addWin();
                    GameTask.fireworkwin.add(w.getPlayer().getLocation());
                    w.getPlayer().setGameMode(GameMode.CREATIVE);
                }
            });
            FPlayerManager.getSpectatingPlayers().forEach(bPlayer -> {
                new PlayerUtils(bPlayer.getPlayer()).sendTitle(5, 20, 5, "§c§LPERDU", "§rVous avez perdu");
            });
        }
    }

    public void spawnBonus() {
        Location location = bonuspoints.get(new Random().nextInt(bonuspoints.size() - 1));
        Block block = location.getBlock();
        if (getBonuscache().contains(block) || location.getY() + 10 > getMaxYPlayers()) {
            boolean canspawn = false;
            for (Location loc : bonuspoints){
                if (loc.getY() + 10 < getMaxYPlayers()){
                    canspawn = true;
                    break;
                }
            }
            if (!canspawn)
                return;
            spawnBonus();
            return;
        }
        block.setType(WOOL);
        bonuscache.add(block);
        PlayerUtils util = new PlayerUtils(FPlayerManager.getAllPlayers());
        util.sendSound(Sound.LEVEL_UP, 2f);
        Bukkit.broadcastMessage("§c§lCRAFTOK §8§l» §7Un bonus vient d'apparaître !");
        List<Byte> colors = new ArrayList<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (colors.isEmpty()) {
                    for (DyeColor d : DyeColor.values())
                        colors.add(d.getWoolData());
                }
                if (block.getType() != WOOL) {
                    cancel();
                    return;
                }
                block.setData(colors.remove(0));
            }
        }.runTaskTimer(FMain.getInstance(), 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getBonuscache().contains(block))
                    spawnBonus();
            }
        }.runTaskLater(FMain.getInstance(), 60 * 20);
    }

    private int getMaxYPlayers(){
        int y = 0;
        for (Player p : FPlayerManager.getPlayingPlayers()){
            if (p.getLocation().getY() > y)
                y = (int) p.getLocation().getY();
        }
        return y + 10;
    }

    public Location getSpawn() {
        return spawn;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public String getMap() {
        return map;
    }

    public int getMaxplayers() {
        return maxplayers;
    }

    public int getMinplayers() {
        return minplayers;
    }

    public void setMinplayers(int minplayers) {
        this.minplayers = minplayers;
    }

    public List<Block> getBonuscache() {
        return bonuscache;
    }

    public DeathMatch getDeathmatch() {
        return deathmatch;
    }

    public enum STATE {LOBBY, GAME, FINISH}
}
/*
 on spawn chacun a des coord
 timer 20 minutes
 win si une seul personne
 faire les fireball qui se lance comme j'ai fait avec le smash et mario
 stats kill, totalmorts, totalwins, totalkills, temps de jeu, nombre de fireball lancer total


*/