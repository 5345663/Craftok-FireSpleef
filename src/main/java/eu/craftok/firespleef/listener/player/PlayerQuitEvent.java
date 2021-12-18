package eu.craftok.firespleef.listener.player;

import eu.craftok.firespleef.manager.player.FPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        FPlayerManager.getFPlayerByPlayer(event.getPlayer()).logout(event);
    }
}
