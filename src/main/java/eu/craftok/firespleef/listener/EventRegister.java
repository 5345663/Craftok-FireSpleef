package eu.craftok.firespleef.listener;

import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.listener.player.PlayerDamageEvent;
import eu.craftok.firespleef.listener.player.PlayerInventoryEvent;
import eu.craftok.firespleef.listener.player.PlayerJoinEvent;
import eu.craftok.firespleef.listener.player.PlayerQuitEvent;
import eu.craftok.firespleef.listener.world.WorldCancelEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class EventRegister {

    public static void registerEvent() {
        PluginManager pm = Bukkit.getPluginManager();
        FMain main = FMain.getInstance();
        pm.registerEvents(new WorldCancelEvent(), main);
        pm.registerEvents(new PlayerDamageEvent(), main);
        pm.registerEvents(new PlayerJoinEvent(), main);
        pm.registerEvents(new PlayerQuitEvent(), main);
        pm.registerEvents(new PlayerInventoryEvent(), main);
    }

}
