package eu.craftok.firespleef.listener.player;

import eu.craftok.firespleef.manager.player.FPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        (new FPlayer(event.getPlayer().getUniqueId())).login(event);
    }
}
