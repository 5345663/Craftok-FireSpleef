package eu.craftok.firespleef.manager.player;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class FPlayerManager {

    public static List<FPlayer> players = new ArrayList<>();
    public static List<FPlayer> playersjumpcache = new ArrayList<>();

    public static List<FPlayer> getPlayingFPlayers() {
        List<FPlayer> values = new ArrayList<>();
        players.forEach(j -> {
            if (j.isPlaying()) values.add(j);
        });
        return values;
    }

    public static List<Player> getPlayingPlayers() {
        List<Player> values = new ArrayList<>();
        players.forEach(j -> {
            if (j.isPlaying() && j.getPlayer() != null) values.add(j.getPlayer());
        });
        return values;
    }

    public static List<FPlayer> getSpectatingFPlayers() {
        List<FPlayer> values = new ArrayList<>();
        players.forEach(j -> {
            if (!j.isPlaying()) values.add(j);
        });
        return values;
    }

    public static List<Player> getSpectatingPlayers() {
        List<Player> values = new ArrayList<>();
        players.forEach(j -> {
            if (!j.isPlaying()) values.add(j.getPlayer());
        });
        return values;
    }

    public static List<Player> getAllPlayers() {
        List<Player> values = new ArrayList<>();
        players.forEach(j -> {
            values.add(j.getPlayer());
        });
        return values;
    }

    public static FPlayer getFPlayerByPlayer(Player player) {
        for (FPlayer DPlayers : players) {
            if (DPlayers.getPlayer().getUniqueId().compareTo(player.getUniqueId()) == 0) {
                return DPlayers;
            }
        }

        return null;
    }
}
